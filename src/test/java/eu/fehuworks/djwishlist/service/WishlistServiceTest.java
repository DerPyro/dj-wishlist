package eu.fehuworks.djwishlist.service;

import static org.junit.jupiter.api.Assertions.*;

import eu.fehuworks.djwishlist.model.User;
import eu.fehuworks.djwishlist.model.Vote;
import eu.fehuworks.djwishlist.model.Wish;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WishlistServiceTest {

  @Mock private UserService userService;

  private WishlistService sut;

  @BeforeEach
  void setUp() {
    sut = new WishlistService(userService);
  }

  @Test
  void wishes_are_initially_empty() {
    assertTrue(sut.getWishes().isEmpty(), "Wishes are not empty");
  }

  @Test
  void add_adds_given_wish() {
    Wish expected = createWish();

    sut.addWish(expected);

    assertEquals(1, sut.getWishes().size(), "Wishes do not contain exactly one wish");
    assertTrue(sut.getWishes().containsKey(expected), "Wishes do not contain exactly added wish");
  }

  private static Wish createWish() {
    return createWish(createUser(UserType.NORMAL));
  }

  private static Wish createWish(User user) {
    Wish wish = new Wish();
    wish.setId(UUID.randomUUID());
    wish.setSongName(UUID.randomUUID().toString());
    wish.setArtist(UUID.randomUUID().toString());
    wish.setIssuer(user.getName());
    wish.setIssuerId(user.getSessionId());
    return wish;
  }

  @Test
  void add_adds_given_wish_with_neutral_vote() {
    Wish wish = createWish();
    sut.addWish(wish);

    Vote result = sut.getWishes().get(wish);

    assertEquals(0, result.getTotalVotes());
    assertEquals(0, result.getUpvotes());
    assertEquals(0, result.getDownvotes());
  }

  @Test
  void removeWish_removes_wish_with_given_id_when_user_is_admin() {
    Wish wish = createWish();
    sut.addWish(wish);

    boolean result = sut.removeWish(wish.getId(), createUser(UserType.ADMIN));

    assertTrue(result, "Did not return true even when removing wish as admin");
    assertFalse(sut.getWishes().containsKey(wish), "Wish was not removed");
  }

  private static User createUser(UserType userType) {
    return new User(
        UUID.randomUUID().toString(), UUID.randomUUID().toString(), userType == UserType.ADMIN);
  }

  enum UserType {
    ADMIN,
    NORMAL
  }

  @Test
  void removeWish_removes_wish_with_given_id_when_user_sessionId_is_issuerId() {
    User user = createUser(UserType.NORMAL);
    Wish wish = createWish(user);
    assertEquals(user.getSessionId(), wish.getIssuerId());
    sut.addWish(wish);

    boolean result = sut.removeWish(wish.getId(), user);

    assertTrue(result, "Did not return true even when removing wish as issuer");
    assertFalse(sut.getWishes().containsKey(wish), "Wish was not removed");
  }

  @Test
  void removeWish_does_not_remove_wish_with_given_id_when_user_sessionId_is_not_issuerId() {
    User user = createUser(UserType.NORMAL);
    Wish wish = createWish(user);
    wish.setIssuerId("other");
    assertNotEquals(user.getSessionId(), wish.getIssuerId());
    sut.addWish(wish);

    boolean result = sut.removeWish(wish.getId(), user);

    assertFalse(result, "Did not return false even when trying to remove as other non-admin user");
    assertTrue(sut.getWishes().containsKey(wish), "Wish was removed");
  }

  @Test
  void removeWish_returns_false_when_no_wish_with_given_id_is_found() {
    sut.addWish(createWish());

    assertFalse(
        sut.removeWish(UUID.randomUUID(), createUser(UserType.NORMAL)),
        "Did return true even without any removed wish");
    assertEquals(1, sut.getWishes().size(), "Did not keep wish");
  }
}
