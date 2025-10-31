package eu.fehuworks.djwishlist.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

  private String sessionId;
  private String name;
  private boolean isAdmin;
}
