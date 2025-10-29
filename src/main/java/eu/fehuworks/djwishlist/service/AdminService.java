package eu.fehuworks.djwishlist.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

  private final UserService userService;

  // TODO: Move to secure storage
  private static final String ADMIN_USERNAME = "admin";
  private static final String ADMIN_PASSWORD = "password123";

  public boolean authenticate(String username, String password, String sessionId) {
    if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
      log.info("Admin Session Id: {}", sessionId);
      userService.registerAdmin(sessionId, username);
      return true;
    } else {
      return false;
    }
  }
}
