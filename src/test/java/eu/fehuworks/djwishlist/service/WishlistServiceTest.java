package eu.fehuworks.djwishlist.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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

  @Test
  void removeWish_keeps_other_wishes_when_removing_wish_with_given_id() {
    Wish wish1 = createWish();
    Wish wish2 = createWish();
    sut.addWish(wish1);
    sut.addWish(wish2);

    boolean result = sut.removeWish(wish1.getId(), createUser(UserType.ADMIN));

    assertTrue(result, "Did not return true when removing existing wish");
    assertFalse(sut.getWishes().containsKey(wish1), "Wish1 was not removed");
    assertTrue(sut.getWishes().containsKey(wish2), "Wish2 was removed");
  }

  @Test
  void upvote_adds_upvote_to_wish_with_given_id() {
    Wish wish = createWish();
    sut.addWish(wish);

    boolean result = sut.upvote(wish.getId(), "upvoter1");
    assertTrue(result, "Did not return true when upvoting existing wish");
    Vote vote = sut.getWishes().get(wish);
    assertEquals(1, vote.getUpvotes(), "Upvotes count is not 1 after one upvote");
    assertEquals(0, vote.getDownvotes(), "Downvotes count is not 0 after one upvote");
    assertEquals(1, vote.getTotalVotes(), "Total votes count is not 1 after one upvote");
  }

  @Test
  void upvote_returns_false_when_no_wish_with_given_id_is_found() {
    sut.addWish(createWish());

    boolean result = sut.upvote(UUID.randomUUID(), "upvoter1");

    assertFalse(result, "Did return true even without any upvoted wish");
  }

  @Test
  void upvote_does_not_upvote_wish_twice_by_same_user() {
    Wish wish = createWish();
    sut.addWish(wish);
    sut.upvote(wish.getId(), "upvoter1");

    boolean result = sut.upvote(wish.getId(), "upvoter1");

    assertTrue(result, "Did not return true when upvoting existing wish");
    Vote vote = sut.getWishes().get(wish);
    assertEquals(0, vote.getUpvotes(), "Upvotes count is not 0 after two upvotes by same user");
    assertEquals(0, vote.getDownvotes(), "Downvotes count is not 0 after two upvotes by same user");
    assertEquals(
        0, vote.getTotalVotes(), "Total votes count is not 0 after two upvotes by same user");
  }

  @Test
  void upvote_can_increase_total_votes_above_one() {
    Wish wish = createWish();
    sut.addWish(wish);

    sut.upvote(wish.getId(), "upvoter1");
    sut.upvote(wish.getId(), "upvoter2");

    Vote result = sut.getWishes().get(wish);
    assertEquals(
        2, result.getUpvotes(), "Upvotes count is not 2 after two upvotes by different users");
    assertEquals(
        0, result.getDownvotes(), "Downvotes count is not 0 after two upvotes by different users");
    assertEquals(
        2,
        result.getTotalVotes(),
        "Total votes count is not 2 after two upvotes by different users");
  }

  @Test
  void downvote_adds_downvote_to_wish_with_given_id() {
    Wish wish = createWish();
    sut.addWish(wish);

    boolean result = sut.downvote(wish.getId(), "downvoter1");
    assertTrue(result, "Did not return true when downvoting existing wish");
    Vote vote = sut.getWishes().get(wish);
    assertEquals(1, vote.getDownvotes(), "Downvotes count is not 1 after one downvote");
    assertEquals(0, vote.getUpvotes(), "Upvotes count is not 0 after one downvote");
    assertEquals(-1, vote.getTotalVotes(), "Total votes count is not -1 after one downvote");
  }

  @Test
  void downvote_returns_false_when_no_wish_with_given_id_is_found() {
    sut.addWish(createWish());

    boolean result = sut.downvote(UUID.randomUUID(), "downvoter1");

    assertFalse(result, "Did return true even without any downvoted wish");
  }

  @Test
  void downvote_does_not_downvote_wish_twice_by_same_user() {
    Wish wish = createWish();
    sut.addWish(wish);
    sut.downvote(wish.getId(), "downvoter1");

    boolean result = sut.downvote(wish.getId(), "downvoter1");

    assertTrue(result, "Did not return true when downvoting existing wish");
    Vote vote = sut.getWishes().get(wish);
    assertEquals(
        0, vote.getDownvotes(), "Downvotes count is not 0 after two downvotes by same user");
    assertEquals(0, vote.getUpvotes(), "Upvotes count is not 0 after two downvotes by same user");
    assertEquals(
        0, vote.getTotalVotes(), "Total votes count is not 0 after two downvotes by same user");
  }

  @Test
  void downvote_can_decrease_total_votes_below_one() {
    Wish wish = createWish();
    sut.addWish(wish);

    sut.downvote(wish.getId(), "downvoter1");
    sut.downvote(wish.getId(), "downvoter2");

    Vote result = sut.getWishes().get(wish);
    assertEquals(
        2,
        result.getDownvotes(),
        "Downvotes count is not 2 after two downvotes by different users");
    assertEquals(
        0, result.getUpvotes(), "Upvotes count is not 0 after two downvotes by different users");
    assertEquals(
        -2,
        result.getTotalVotes(),
        "Total votes count is not -2 after two downvotes by different users");
  }

  @Test
  void updateUsernames_updates_issuer_names_based_on_user_service() {
    String expected = "NewUsername";
    User user = createUser(UserType.NORMAL);
    when(userService.getUser(user.getSessionId()))
        .thenReturn(new User(user.getSessionId(), expected, false));

    Wish wish = createWish(user);
    sut.addWish(wish);
    assertEquals(user.getName(), wish.getIssuer(), "Issuer name was not updated");

    sut.updateUsernames(user.getSessionId());

    assertEquals(expected, wish.getIssuer(), "Issuer name was not updated");
  }
}
