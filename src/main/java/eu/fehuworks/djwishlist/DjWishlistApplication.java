package eu.fehuworks.djwishlist;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class DjWishlistApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(DjWishlistApplication.class).run(args);
  }
}
