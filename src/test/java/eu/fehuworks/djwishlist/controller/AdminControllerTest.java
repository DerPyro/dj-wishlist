package eu.fehuworks.djwishlist.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import eu.fehuworks.djwishlist.model.AdminUser;
import eu.fehuworks.djwishlist.service.AdminService;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

  @MockitoBean private AdminService adminService;
  @Autowired private MockMvc mockMvc;

  @Test
  void showAdminPage_return_admin() throws Exception {
    mockMvc
        .perform(get("/admin"))
        .andExpect(status().isOk())
        .andExpect(view().name("admin"))
        .andExpect(
            model()
                .attribute(
                    "adminUser",
                    allOf(
                        hasProperty("username", nullValue()),
                        hasProperty("password", nullValue()))));
  }

  @Test
  void handleAdminLogin_calls_adminService_authenticate_with_given_adminUser_and_sessionId()
      throws Exception {
    AdminUser adminUser = createAdminUser();
    MockHttpSession session = createHttpSession();
    mockMvc
        .perform(post("/admin").flashAttr("adminUser", adminUser).session(session))
        .andExpect(status().is3xxRedirection());

    verify(adminService)
        .authenticate(adminUser.getUsername(), adminUser.getPassword(), session.getId());
  }

  private static AdminUser createAdminUser() {
    AdminUser adminUser = new AdminUser();
    adminUser.setUsername("username-" + UUID.randomUUID());
    adminUser.setPassword("password-" + UUID.randomUUID());
    return adminUser;
  }

  private static MockHttpSession createHttpSession() {
    return new MockHttpSession(null, "sessionId-" + UUID.randomUUID());
  }

  @ParameterizedTest
  @MethodSource("handleAdminLogin_test_data")
  void handleAdminLogin_redirection_is_conditional_from_adminService_authenticate_call(
      boolean adminServiceResult, String expectedRedirectedUrl) throws Exception {
    when(adminService.authenticate(any(), any(), any())).thenReturn(adminServiceResult);

    mockMvc
        .perform(
            post("/admin").flashAttr("adminUser", createAdminUser()).session(createHttpSession()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(expectedRedirectedUrl));
  }

  private static Stream<Arguments> handleAdminLogin_test_data() {
    return Stream.of(Arguments.of(true, "/"), Arguments.of(false, "/admin"));
  }
}
