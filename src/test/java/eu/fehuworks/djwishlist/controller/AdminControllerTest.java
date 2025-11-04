package eu.fehuworks.djwishlist.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import eu.fehuworks.djwishlist.model.AdminUser;
import eu.fehuworks.djwishlist.service.AdminService;
import jakarta.servlet.http.HttpSession;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

  @Mock private AdminService adminService;
  @Mock private Model model;
  @Mock private HttpSession httpSession;

  private AdminController sut;

  @BeforeEach
  void setUp() {
    sut = new AdminController(adminService);
  }

  @Test
  void showAdminPage_return_admin() {
    assertEquals("admin", sut.showAdminPage(model));
  }

  @Test
  void showAdminPage_adds_new_adminUser_to_model() {
    sut.showAdminPage(model);

    verify(model).addAttribute("adminUser", new AdminUser());
  }

  @Test
  void handleAdminLogin_calls_adminService_authenticate_with_given_adminUser_and_sessionId() {
    AdminUser adminUser = createAdminUser();
    String expectedSessionId = UUID.randomUUID().toString();
    when(httpSession.getId()).thenReturn(expectedSessionId);

    sut.handleAdminLogin(adminUser, httpSession);

    verify(adminService)
        .authenticate(adminUser.getUsername(), adminUser.getPassword(), expectedSessionId);
  }

  private static AdminUser createAdminUser() {
    AdminUser adminUser = new AdminUser();
    adminUser.setUsername(UUID.randomUUID().toString());
    adminUser.setPassword(UUID.randomUUID().toString());
    return adminUser;
  }

  @ParameterizedTest
  @MethodSource("handleAdminLogin_test_data")
  void handleAdminLogin_return_is_conditional_from_adminService_authenticate_call(
      boolean adminServiceResult, String expectedResult) {
    when(adminService.authenticate(any(), any(), any())).thenReturn(adminServiceResult);

    String result = sut.handleAdminLogin(createAdminUser(), httpSession);

    assertEquals(expectedResult, result);
  }

  private static Stream<Arguments> handleAdminLogin_test_data() {
    return Stream.of(Arguments.of(true, "redirect:/"), Arguments.of(false, "redirect:/admin"));
  }
}
