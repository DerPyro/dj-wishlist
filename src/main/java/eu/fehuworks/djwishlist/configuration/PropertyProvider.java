package eu.fehuworks.djwishlist.configuration;

import eu.fehuworks.djwishlist.constant.PropertyConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class PropertyProvider {

  @Bean(PropertyConstants.ADMIN_USERNAME)
  String adminUsername(@Value(PropertyConstants.SpringElConstants.ADMIN_USERNAME) String username) {
    log.info("Using '{}' as admin username", username);
    return username;
  }

  @Bean(PropertyConstants.ADMIN_PASSWORD)
  String adminPassword(@Value(PropertyConstants.SpringElConstants.ADMIN_PASSWORD) String password) {
    log.info("Using '{}' as admin password", password);
    return password;
  }
}
