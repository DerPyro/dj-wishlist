package eu.fehuworks.djwishlist.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

  private String sessionId;
  private String name;
  private boolean isAdmin;

  public User(String sessionId, String name) {
    this.sessionId = sessionId;
    this.name = nameOrDefault(name, sessionId);
  }

  private static String nameOrDefault(String name, String sessionId) {
    return (name == null || name.isBlank()) ? "Guest-" + sessionId : name;
  }
}
