package eu.fehuworks.djwishlist.controller.mvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import eu.fehuworks.djwishlist.model.User;
import eu.fehuworks.djwishlist.model.Vote;
import eu.fehuworks.djwishlist.model.Wish;
import eu.fehuworks.djwishlist.service.UserService;
import eu.fehuworks.djwishlist.service.WishlistService;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(GuestController.class)
class GuestControllerTest {

  @MockitoBean private WishlistService wishlistService;
  @MockitoBean private UserService userService;
  @Autowired private MockMvc mockMvc;
  @Captor private ArgumentCaptor<Wish> wishCaptor;

  @BeforeEach
  void setUp() {
    // TODO: remove this when @Captor-Annotation works again
    wishCaptor = ArgumentCaptor.forClass(Wish.class);
  }

  @Test
  void openStartPage_redirect_to_userController_when_userService_isUserKnown_returns_false()
      throws Exception {
    MockHttpSession httpSession = createHttpSession();
    when(userService.isUserKnown(any())).thenReturn(false);

    mockMvc
        .perform(get("/").session(httpSession))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/user"));

    verify(userService).isUserKnown(httpSession.getId());
  }

  private static MockHttpSession createHttpSession() {
    return new MockHttpSession(null, "sessionId-" + UUID.randomUUID());
  }

  @Test
  void openStartPage_returns_start_view_when_userService_isUserKnown_returns_true()
      throws Exception {
    MockHttpSession httpSession = createHttpSession();
    when(userService.isUserKnown(any())).thenReturn(true);
    User user = createUser("userSessionId-" + UUID.randomUUID());
    when(userService.getUser(any())).thenReturn(user);

    mockMvc
        .perform(get("/").session(httpSession))
        .andExpect(status().isOk())
        .andExpect(view().name("start"))
        .andExpect(
            model()
                .attribute(
                    "user",
                    allOf(
                        hasProperty("name", is(user.getName())),
                        hasProperty("sessionId", is(user.getSessionId())),
                        not(hasProperty("isAdmin")))));

    verify(userService).getUser(httpSession.getId());
  }

  private static User createUser(String sessionId) {
    User user = new User();
    user.setName("name-" + UUID.randomUUID());
    user.setSessionId(sessionId);
    user.setAdmin(false);
    return user;
  }

  @Test
  void openStartPage_adds_mostly_empty_wish_to_model() throws Exception {
    when(userService.isUserKnown(any())).thenReturn(true);
    User user = createUser("userSessionId-" + UUID.randomUUID());
    when(userService.getUser(any())).thenReturn(user);

    mockMvc
        .perform(get("/"))
        .andExpect(
            model()
                .attribute(
                    "newWish",
                    allOf(
                        hasProperty("issuer", is(user.getName())),
                        hasProperty("id", nullValue()),
                        hasProperty("songName", nullValue()),
                        hasProperty("issuerId", nullValue()))));
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void openStartPage_adds_sessionId_and_isAdmin_values_to_model(boolean isAdmin) throws Exception {
    MockHttpSession httpSession = createHttpSession();
    when(userService.isUserKnown(any())).thenReturn(true);
    User user = createUser("userSessionId-" + UUID.randomUUID());
    user.setAdmin(isAdmin);
    when(userService.getUser(any())).thenReturn(user);

    mockMvc
        .perform(get("/").session(httpSession))
        .andExpect(model().attribute("sessionId", is(httpSession.getId())))
        .andExpect(model().attribute("isAdmin", is(user.isAdmin())));
  }

  @Test
  void openStartPage_returns_model_with_wishlist_sorted_by_votes() throws Exception {
    when(userService.isUserKnown(any())).thenReturn(true);
    when(userService.getUser(any())).thenReturn(createUser("userSessionId-" + UUID.randomUUID()));
    Wish wishWithNeutralVotes = createWish();
    Wish wishWithNegativeVotes = createWish();
    Wish wishWithPositiveVotes = createWish();
    Map<Wish, Vote> wishlist =
        Map.of(
            wishWithNeutralVotes, new Vote(5, 5),
            wishWithNegativeVotes, new Vote(4, 10),
            wishWithPositiveVotes, new Vote(13, 1));
    when(wishlistService.getWishes()).thenReturn(wishlist);

    mockMvc
        .perform(get("/"))
        .andExpect(
            model()
                .attribute(
                    "wishlist",
                    is(
                        Map.of(
                            wishWithPositiveVotes, new Vote(13, 1),
                            wishWithNeutralVotes, new Vote(5, 5),
                            wishWithNegativeVotes, new Vote(4, 10)))));
  }

  @Test
  void upvote_calls_wishlistService_upvote_with_given_songId_and_sessionId() throws Exception {
    MockHttpSession httpSession = createHttpSession();
    UUID expectedSongId = UUID.randomUUID();

    mockMvc
        .perform(post("/upvote/" + expectedSongId).session(httpSession))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"));

    verify(wishlistService).upvote(expectedSongId, httpSession.getId());
  }

  @Test
  void downvote_calls_wishlistService_downvote_with_given_songId_and_sessionId() throws Exception {
    MockHttpSession httpSession = createHttpSession();
    UUID expectedSongId = UUID.randomUUID();

    mockMvc
        .perform(get("/downvote/" + expectedSongId).session(httpSession))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"));

    verify(wishlistService).downvote(expectedSongId, httpSession.getId());
  }

  @Test
  void delete_calls_wishlistService_removeWish_with_given_song_and_sessionId() throws Exception {
    User user = createUser("userSessionId-" + UUID.randomUUID());
    MockHttpSession httpSession = createHttpSession();
    when(userService.getUser(httpSession.getId())).thenReturn(user);
    UUID expectedSongId = UUID.randomUUID();

    mockMvc
        .perform(get("/delete/" + expectedSongId).session(httpSession))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"));

    verify(wishlistService).removeWish(expectedSongId, user);
  }

  @Test
  void addWish_calls_userService_add_with_given_sessionId_and_with_issuer_to_update_name()
      throws Exception {
    MockHttpSession httpSession = createHttpSession();
    String expectedIssuer = UUID.randomUUID().toString();
    Wish wish = new Wish();
    wish.setIssuer(expectedIssuer);

    mockMvc.perform(post("/").session(httpSession).flashAttr("wish", wish));

    InOrder inOrder = inOrder(userService, wishlistService);
    inOrder.verify(userService).add(httpSession.getId(), wish.getIssuer());
    inOrder.verify(wishlistService).updateUsernames(httpSession.getId());
  }

  @Test
  void addWish_calls_wishlistService_with_given_wish_and_random_id_and_issuerId_set_to_sessionId()
      throws Exception {
    MockHttpSession httpSession = createHttpSession();
    Wish wish = createWish();
    wish.setId(null);

    mockMvc.perform(post("/").session(httpSession).flashAttr("wish", wish));

    verify(wishlistService).addWish(wishCaptor.capture());
    Wish result = wishCaptor.getValue();
    assertEquals(wish.getSongName(), result.getSongName(), "SongName was not correct mapped");
    assertEquals(wish.getArtist(), result.getArtist(), "Artist was not correct mapped");
    assertEquals(wish.getIssuer(), result.getIssuer(), "Issuer was not correct mapped");
    assertEquals(
        httpSession.getId(), result.getIssuerId(), "IssuerId is not filled with sessionId");
    assertNotNull(result.getId(), "Id was not generated");
  }

  private static Wish createWish() {
    Wish wish = new Wish();
    wish.setId(UUID.randomUUID());
    wish.setSongName("songName-" + UUID.randomUUID());
    wish.setArtist("artist-" + UUID.randomUUID());
    wish.setIssuer("issuer-" + UUID.randomUUID());
    wish.setIssuerId("issuerId-" + UUID.randomUUID());
    return wish;
  }

  @Test
  void addWish_reloads_page() throws Exception {
    mockMvc
        .perform(post("/").flashAttr("wish", createWish()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"));
  }
}
