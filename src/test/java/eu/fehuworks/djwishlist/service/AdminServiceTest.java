package eu.fehuworks.djwishlist.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

  private AdminService sut;
  @Mock private UserService userService;

  @BeforeEach
  void setUp() {
    sut = new AdminService(userService, "admin", "password123");
  }

  @Test
  void authenticate_validCredentials_returnsTrue() {
    var expectedUsername = UUID.randomUUID().toString();
    var expectedPassword = UUID.randomUUID().toString();

    sut = new AdminService(userService, expectedUsername, expectedPassword);

    assertTrue(sut.authenticate(expectedUsername, expectedPassword, UUID.randomUUID().toString()));
  }

  @Test
  void authenticate_invalidCredentials_returnsFalse() {
    assertFalse(sut.authenticate("noAdmin", "", UUID.randomUUID().toString()));
  }

  @Test
  void authenticate_registerAdmin_in_userService_when_validCredentials() {
    var expectedSessionId = UUID.randomUUID().toString();
    sut.authenticate("admin", "password123", expectedSessionId);

    verify(userService).registerAdmin(expectedSessionId, "admin");
  }

  @Test
  void authenticate_can_handle_null() {
    assertDoesNotThrow(() -> sut.authenticate(null, null, null));
  }
}
