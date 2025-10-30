package eu.fehuworks.djwishlist.service;

import eu.fehuworks.djwishlist.constant.PropertyConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdminService {

  @Autowired
  public AdminService(
      UserService userService,
      @Qualifier(PropertyConstants.ADMIN_USERNAME) String adminUsername,
      @Qualifier(PropertyConstants.ADMIN_PASSWORD) String adminPassword) {
    this.userService = userService;
    this.adminUsername = adminUsername;
    this.adminPassword = adminPassword;
  }

  private final UserService userService;
  private final String adminUsername;
  private final String adminPassword;

  public boolean authenticate(String username, String password, String sessionId) {
    if (adminUsername.equals(username) && adminPassword.equals(password)) {
      log.info("Admin Session Id: {}", sessionId);
      userService.registerAdmin(sessionId, username);
      return true;
    } else {
      return false;
    }
  }
}
