package eu.fehuworks.djwishlist.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertyConstants {

  public static final String ADMIN_USERNAME = "adminUsername";
  public static final String ADMIN_PASSWORD = "adminPassword";

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class SpringElConstants {

    public static final String ADMIN_USERNAME = "${admin.username:admin}";
    public static final String ADMIN_PASSWORD = "${admin.password:password123}";
  }
}
