package eu.fehuworks.djwishlist.service;

import eu.fehuworks.djwishlist.model.User;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final Map<String, User> users = new ConcurrentHashMap<>();
  private String adminSessionId = null;

  public void add(String sessionId, String name) {
    users.put(sessionId, new User(sessionId, name));
  }

  public void registerAdmin(String sessionId, String name) {
    adminSessionId = sessionId;
    add(sessionId, name);
  }

  public User getUser(String sessionId) {
    var user = users.getOrDefault(sessionId, new User(sessionId, null, false));
    if (sessionId.equals(adminSessionId)) {
      user.setAdmin(true);
    }
    return user;
  }

  public boolean isUserKnown(String sessionId) {
    return users.containsKey(sessionId);
  }
}
