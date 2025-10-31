package eu.fehuworks.djwishlist.service;

import static org.junit.jupiter.api.Assertions.*;

import eu.fehuworks.djwishlist.model.User;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserServiceTest {

  private UserService sut;

  @BeforeEach
  void setUp() {
    sut = new UserService();
  }

  @Test
  void getUser_returns_default_user_with_given_sessionId() {
    String expectedSessionId = UUID.randomUUID().toString();

    User result = sut.getUser(expectedSessionId);

    assertEquals(expectedSessionId, result.getSessionId(), "session id is not correct");
  }

  @Test
  void getUser_returns_default_user_with_generated_name() {
    String sessionId = UUID.randomUUID().toString();

    User result = sut.getUser(sessionId);

    assertEquals("Guest-" + sessionId, result.getName(), "default name is not correct");
  }

  @Test
  void getUser_returns_default_user_with_isAdmin_false() {
    User result = sut.getUser(UUID.randomUUID().toString());

    assertFalse(result.isAdmin(), "user is admin - but should not be admin");
  }

  @Test
  void add_adds_user_with_given_sessionId_and_name() {
    String expectedSessionId = UUID.randomUUID().toString();
    String expectedName = UUID.randomUUID().toString();

    sut.add(expectedSessionId, expectedName);

    User result = sut.getUser(expectedSessionId);
    assertEquals(expectedSessionId, result.getSessionId(), "session id is not correct");
    assertEquals(expectedName, result.getName(), "name is not correct");
  }

  @Test
  void add_adds_user_with_isAdmin_false() {
    String expectedSessionId = UUID.randomUUID().toString();

    sut.add(expectedSessionId, UUID.randomUUID().toString());

    User result = sut.getUser(expectedSessionId);
    assertFalse(result.isAdmin(), "user is admin - but should not be admin");
  }

  @Test
  void isUserKnown_returns_false_when_no_user_with_given_sessionId_is_added() {
    assertFalse(
        sut.isUserKnown(UUID.randomUUID().toString()), "user is known - but should not be known");
  }

  @Test
  void isUserKnown_returns_true_when_a_user_with_given_sessionId_is_added() {
    String expectedSessionId = UUID.randomUUID().toString();

    sut.add(expectedSessionId, UUID.randomUUID().toString());

    assertTrue(sut.isUserKnown(expectedSessionId), "user is not known - but should be known");
  }

  @Test
  void registerAdmin_adds_user_with_isAdmin_true() {
    String expectedSessionId = UUID.randomUUID().toString();

    sut.registerAdmin(expectedSessionId, UUID.randomUUID().toString());

    User result = sut.getUser(expectedSessionId);
    assertTrue(result.isAdmin(), "user is not admin - but should be admin");
  }

  @Test
  void when_user_is_already_admin_add_does_not_set_isAdmin_false() {
    String expectedSessionId = UUID.randomUUID().toString();
    String name = UUID.randomUUID().toString();

    sut.registerAdmin(expectedSessionId, name);
    sut.add(expectedSessionId, name);

    User result = sut.getUser(expectedSessionId);
    assertTrue(result.isAdmin(), "user is not longer admin - but should still be admin");
  }

  @Test
  void only_one_user_can_have_isAdmin_true_at_a_time() {
    String user1SessionId = UUID.randomUUID().toString();
    String user2SessionId = UUID.randomUUID().toString();

    sut.registerAdmin(user1SessionId, "user1");
    assertTrue(sut.getUser(user1SessionId).isAdmin(), "user1 is not admin - but should be admin");

    sut.registerAdmin(user2SessionId, "user2");
    assertTrue(sut.getUser(user2SessionId).isAdmin(), "user2 is not admin - but should be admin");
    assertFalse(
        sut.getUser(user1SessionId).isAdmin(), "user1 is still admin - but should not be admin");
  }
}
