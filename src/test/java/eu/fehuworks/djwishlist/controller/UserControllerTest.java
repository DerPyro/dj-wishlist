package eu.fehuworks.djwishlist.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import eu.fehuworks.djwishlist.model.User;
import eu.fehuworks.djwishlist.service.UserService;
import eu.fehuworks.djwishlist.service.WishlistService;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
class UserControllerTest {

  @MockitoBean private UserService userService;
  @MockitoBean private WishlistService wishlistService;
  @Autowired private MockMvc mockMvc;

  @Test
  void
      openUserCreationPage_return_user_view_with_user_from_userService_getUser_with_given_sessionId()
          throws Exception {
    MockHttpSession httpSession = createHttpSession();
    User user = createUser("userSessionId-" + UUID.randomUUID());
    when(userService.getUser(anyString())).thenReturn(user);

    mockMvc
        .perform(get("/user").session(httpSession))
        .andExpect(status().isOk())
        .andExpect(view().name("user"))
        .andExpect(
            model()
                .attribute(
                    "user",
                    allOf(
                        hasProperty("name", is(user.getName())),
                        hasProperty("sessionId", is(user.getSessionId())))));

    verify(userService).getUser(httpSession.getId());
  }

  private static User createUser(String sessionId) {
    User user = new User();
    user.setName("name-" + UUID.randomUUID());
    user.setSessionId(sessionId);
    user.setAdmin(false);
    return user;
  }

  private static MockHttpSession createHttpSession() {
    return new MockHttpSession(null, "sessionId-" + UUID.randomUUID());
  }

  @Test
  void addUser_adds_given_user_to_userService_with_given_sessionId() throws Exception {
    MockHttpSession httpSession = createHttpSession();
    User user = createUser("ignoreThisValue");

    mockMvc.perform(post("/user").session(httpSession).flashAttr("user", user));

    verify(userService).add(httpSession.getId(), user.getName());
  }

  @Test
  void addUser_calls_updateUsernames_with_given_sessionId_after_adding_user() throws Exception {
    MockHttpSession httpSession = createHttpSession();

    mockMvc.perform(
        post("/user").session(httpSession).flashAttr("user", createUser("ignoreThisValue")));

    InOrder inOrder = inOrder(userService, wishlistService);
    inOrder.verify(userService).add(anyString(), anyString());
    inOrder.verify(wishlistService).updateUsernames(httpSession.getId());
  }

  @Test
  void addUser_redirects_to_start() throws Exception {
    mockMvc
        .perform(
            post("/user")
                .session(createHttpSession())
                .flashAttr("user", createUser("ignoreThisValue")))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"));
  }
}
