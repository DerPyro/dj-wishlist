package eu.fehuworks.djwishlist.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class VoteTest {

  private static final Random RANDOM = new Random();

  @Test
  void getTotalVotes_subtracts_downvotes_from_upvotes() {
    int upvotes = RANDOM.nextInt();
    int downvotes = RANDOM.nextInt();
    int expectedTotalVotes = upvotes - downvotes;

    Vote sut = new Vote(upvotes, downvotes);

    assertEquals(expectedTotalVotes, sut.getTotalVotes(), "Math is broken - lol");
  }

  @ParameterizedTest
  @MethodSource("compareTo_test_data")
  void compareTo_test(Vote vote, Vote voteToCompare, int value) {
    assertEquals(value, vote.compareTo(voteToCompare));
  }

  private static Stream<Arguments> compareTo_test_data() {
    return Stream.of(
        Arguments.of(new Vote(0, 0), new Vote(0, 0), 0),
        Arguments.of(new Vote(1, 0), new Vote(0, 0), 1),
        Arguments.of(new Vote(0, 1), new Vote(0, 0), -1),
        Arguments.of(new Vote(5, 1), new Vote(0, 0), 1),
        Arguments.of(new Vote(5, 1), new Vote(120000, 0), -1));
  }

  @Test
  void compareTo_throws_NullPointerException_when_comparing_to_null() {
    Vote sut = new Vote(161, 0);

    assertThrows(
        NullPointerException.class,
        () -> sut.compareTo(null),
        "null should not be a valid thing to compare against");
  }
}
