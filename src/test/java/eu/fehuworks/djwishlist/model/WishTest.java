package eu.fehuworks.djwishlist.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class WishTest {

  @Test
  void equals_return_false_when_comparing_to_different_class() {
    Wish sut = new Wish();
    Object toCompare = new Object();
    assertNotEquals(toCompare, sut, "Different classes lead not to inequality");
    assertNotEquals(
        toCompare.hashCode(), sut.hashCode(), "Different classes lead not to different hashCodes");
  }

  @Test
  void equals_return_false_when_comparing_to_null() {
    Wish sut = new Wish();
    assertNotEquals(null, sut, "null comparison does not lead to inequality");
  }

  @Test
  void equals_return_true_when_id_is_equal() {
    Wish sut = new Wish();
    UUID sharedId = UUID.randomUUID();
    sut.setId(sharedId);
    sut.setSongName("Trip to Valhalla");
    sut.setArtist("Dr. Peacock");

    Wish toCompare = new Wish();
    toCompare.setId(sharedId);
    toCompare.setSongName("Word up!");
    toCompare.setArtist("Korn");

    assertEquals(toCompare, sut, "Equal Id does not lead to equal objects");
    assertEquals(toCompare.hashCode(), sut.hashCode(), "Equal Id does not lead to same hashCode");
  }

  @Test
  void equals_return_false_when_id_is_different() {
    Wish sut = new Wish();
    sut.setId(UUID.randomUUID());
    sut.setSongName("Trip to Valhalla");
    sut.setArtist("Dr. Peacock");

    Wish toCompare = new Wish();
    toCompare.setId(UUID.randomUUID());
    toCompare.setSongName("Trip to Valhalla");
    toCompare.setArtist("Dr. Peacock");

    assertNotEquals(toCompare, sut, "Unequal Id does lead to equal objects");
    assertNotEquals(toCompare.hashCode(), sut.hashCode(), "Unequal Id does lead to same hashCode");
  }

  @Test
  void upvote_adds_given_upvoter_to_votes() {
    Wish sut = new Wish();
    assertNull(sut.getVotes());

    String user = UUID.randomUUID().toString();
    sut.upvote(user);

    assertEquals(1, sut.getVotes().size(), "Wrong amount of users is stored in votes");
    assertEquals(
        Wish.VoteType.UPVOTE, sut.getVotes().get(user), "User is not marked with UPVOTE in votes");
  }

  @Test
  void isUpvotedBy_returns_false_if_user_has_not_upvoted() {
    assertFalse(new Wish().isUpvotedBy(UUID.randomUUID().toString()), "User is marked as upvoter");
  }

  @Test
  void isUpvotedBy_returns_true_if_user_has_upvoted() {
    Wish sut = new Wish();

    String user = UUID.randomUUID().toString();
    sut.upvote(user);

    assertTrue(sut.isUpvotedBy(user), "User is not marked as upvoter");
  }

  @Test
  void upvote_removes_given_user_from_votes_if_already_upvoted() {
    Wish sut = new Wish();
    String user = UUID.randomUUID().toString();
    sut.upvote(user);
    assertTrue(sut.isUpvotedBy(user), "User is not marked as upvoter");
    assertFalse(sut.isDownvotedBy(user), "User is marked as downvoter");

    sut.upvote(user);
    assertFalse(sut.isUpvotedBy(user), "User is still marked as upvoter");
    assertFalse(sut.isDownvotedBy(user), "User is marked as downvoter");
  }

  @Test
  void downvote_adds_given_downvoter_to_votes() {
    Wish sut = new Wish();
    assertNull(sut.getVotes());

    String user = UUID.randomUUID().toString();
    sut.downvote(user);

    assertEquals(1, sut.getVotes().size(), "Wrong amount of users is stored in votes");
    assertEquals(
        Wish.VoteType.DOWNVOTE,
        sut.getVotes().get(user),
        "User is not marked with DOWNVOTES in votes");
  }

  @Test
  void isDownvotedBy_returns_false_if_user_has_not_downvoted() {
    assertFalse(
        new Wish().isDownvotedBy(UUID.randomUUID().toString()), "User is marked as downvoter");
  }

  @Test
  void isDownvotedBy_returns_true_if_user_has_downvoted() {
    Wish sut = new Wish();

    String user = UUID.randomUUID().toString();
    sut.downvote(user);

    assertTrue(sut.isDownvotedBy(user), "User is not marked as downvoter");
  }

  @Test
  void downvote_removes_given_user_from_votes_if_already_downvoted() {
    Wish sut = new Wish();
    String user = UUID.randomUUID().toString();
    sut.downvote(user);
    assertTrue(sut.isDownvotedBy(user), "User is not marked as downvoter");
    assertFalse(sut.isUpvotedBy(user), "User is marked as upvoter");

    sut.downvote(user);
    assertFalse(sut.isDownvotedBy(user), "User is still marked as downvoter");
    assertFalse(sut.isUpvotedBy(user), "User is marked as upvoter");
  }

  @Test
  void downvote_shifts_voter_from_upvote_to_downvote_if_upvoted_before() {
    Wish sut = new Wish();
    String user = UUID.randomUUID().toString();

    sut.upvote(user);

    assertEquals(1, sut.getVotes().size(), "Wrong amount of users is stored in votes");
    assertEquals(
        Wish.VoteType.UPVOTE, sut.getVotes().get(user), "User is not marked with UPVOTE in votes");

    sut.downvote(user);

    assertEquals(1, sut.getVotes().size(), "Wrong amount of users is stored in votes");
    assertEquals(
        Wish.VoteType.DOWNVOTE,
        sut.getVotes().get(user),
        "User is not marked with DOWNVOTES in votes");
  }

  @Test
  void upvote_shifts_voter_from_downvote_to_upvote_if_downvoted_before() {
    Wish sut = new Wish();
    String user = UUID.randomUUID().toString();

    sut.downvote(user);

    assertEquals(1, sut.getVotes().size(), "Wrong amount of users is stored in votes");
    assertEquals(
        Wish.VoteType.DOWNVOTE,
        sut.getVotes().get(user),
        "User is not marked with DOWNVOTES in votes");

    sut.upvote(user);

    assertEquals(1, sut.getVotes().size(), "Wrong amount of users is stored in votes");
    assertEquals(
        Wish.VoteType.UPVOTE, sut.getVotes().get(user), "User is not marked with UPVOTE in votes");
  }

  @Test
  void canBeDeletedBy_returns_false_when_deleter_is_not_issuerId() {
    Wish sut = new Wish();
    sut.setIssuerId(UUID.randomUUID().toString());

    assertFalse(sut.canBeDeletedBy(UUID.randomUUID().toString()), "Wrong user can delete wish");
  }

  @Test
  void canBeDeletedBy_returns_true_when_deleter_is_issuerId() {
    Wish sut = new Wish();

    String user = UUID.randomUUID().toString();
    sut.setIssuerId(user);

    assertTrue(sut.canBeDeletedBy(user), "Issuer can not delete wish");
  }
}
