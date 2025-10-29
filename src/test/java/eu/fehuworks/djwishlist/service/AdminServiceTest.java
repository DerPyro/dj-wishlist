package eu.fehuworks.djwishlist.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
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
    sut = new AdminService(userService);
  }

  @Test
  void authenticate_validCredentials_returnsTrue() {
    assertTrue(sut.authenticate("admin", "password123", UUID.randomUUID().toString()));
  }

  @Test
  void authenticate_invalidCredentials_returnsFalse() {
    assertFalse(sut.authenticate("noAdmin", "", UUID.randomUUID().toString()));
  }

  @Test
  void authenticate_registerAdmin_in_userService_when_validCredentials() {
    var expectedSessionId = UUID.randomUUID().toString();
    sut.authenticate("admin", "password123", expectedSessionId);

    verify(userService).registerAdmin(eq(expectedSessionId), eq("admin"));
  }

  @Test
  void authenticate_can_handle_null() {
    assertDoesNotThrow(() -> sut.authenticate(null, null, null));
  }
}
