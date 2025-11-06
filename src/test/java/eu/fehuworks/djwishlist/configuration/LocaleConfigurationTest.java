package eu.fehuworks.djwishlist.configuration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@SpringBootTest(classes = LocaleConfiguration.class)
class LocaleConfigurationTest {

  @Autowired private ApplicationContext context;

  @Test
  void context_contains_localeResolver_with_default_value_german() {
    LocaleResolver result = context.getBean(LocaleResolver.class);

    assertEquals(Locale.GERMAN, ReflectionTestUtils.getField(result, "defaultLocale"));
  }

  @Test
  void context_contains_localeChangeInterceptor_with_paramName_lang() {
    LocaleChangeInterceptor result = context.getBean(LocaleChangeInterceptor.class);

    assertEquals("lang", result.getParamName());
  }
}
