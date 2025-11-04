package eu.fehuworks.djwishlist.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import eu.fehuworks.djwishlist.model.User;
import eu.fehuworks.djwishlist.service.UserService;
import eu.fehuworks.djwishlist.service.WishlistService;
import jakarta.servlet.http.HttpSession;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  @Mock private UserService userService;
  @Mock private WishlistService wishlistService;
  @Mock private Model model;
  @Mock private HttpSession httpSession;

  private UserController sut;

  @BeforeEach
  void setUp() {
    sut = new UserController(userService, wishlistService);
  }

  @Test
  void openUserCreationPage_return_user() {
    assertEquals("user", sut.openUserCreationPage(model, httpSession));
  }

  @Test
  void openUserCreationPage_adds_result_of_userService_getUser_with_sessionId_to_model() {
    String expectedSessionId = UUID.randomUUID().toString();
    when(httpSession.getId()).thenReturn(expectedSessionId);
    User expectedUser = createUser(expectedSessionId);
    when(userService.getUser(anyString())).thenReturn(expectedUser);

    sut.openUserCreationPage(model, httpSession);

    verify(userService).getUser(expectedSessionId);
    verify(model).addAttribute("user", expectedUser);
  }

  private static User createUser(String sessionId) {
    User user = new User();
    user.setName(UUID.randomUUID().toString());
    user.setSessionId(sessionId);
    user.setAdmin(false);
    return user;
  }

  @Test
  void addUser_return_redirect_to_base() {
    String result = sut.addUser(createUser(UUID.randomUUID().toString()), httpSession);

    assertEquals("redirect:/", result);
  }

  @Test
  void addUser_calls_userService_add_with_given_sessionId_and_name() {
    String expectedSessionId = UUID.randomUUID().toString();
    when(httpSession.getId()).thenReturn(expectedSessionId);
    User user = createUser(UUID.randomUUID().toString());

    sut.addUser(user, httpSession);

    verify(userService).add(expectedSessionId, user.getName());
  }

  @Test
  void addUSer_calls_wishlistService_updateUsernames_with_given_sessionId() {
    String expectedSessionId = UUID.randomUUID().toString();
    when(httpSession.getId()).thenReturn(expectedSessionId);

    sut.addUser(createUser(UUID.randomUUID().toString()), httpSession);

    verify(wishlistService).updateUsernames(expectedSessionId);
  }
}
