package bio.ferlab.clin.prescription.renderer.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.spring5.messageresolver.SpringMessageResolver;

import java.util.Locale;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
  
  public static final Locale DEFAULT_LOCALE = Locale.FRENCH;
  
  @Autowired
  private SecurityConfiguration securityConfiguration;
  
  @Bean
  public LocaleResolver localeResolver() {
    SessionLocaleResolver slr = new SessionLocaleResolver();
    slr.setDefaultLocale(DEFAULT_LOCALE);
    return slr;
  }

  @Bean
  public LocaleChangeInterceptor localeChangeInterceptor() {
    LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
    lci.setParamName("lang");
    return lci;
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins(securityConfiguration.getCors().toArray(String[]::new))
        .allowedMethods(HttpMethod.GET.name())
        .allowedHeaders(HttpHeaders.AUTHORIZATION)  // request allowed header
        .exposedHeaders(HttpHeaders.CONTENT_DISPOSITION)  // response allowed header
        .allowCredentials(true)
        .maxAge(3600);
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(localeChangeInterceptor());
  }

  @Bean
  public MessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("messages");
    messageSource.setDefaultEncoding("ISO-8859-1"); // JAVA base encoding isn't UTF-8
    messageSource.setFallbackToSystemLocale(false);
    return messageSource;
  }
  
  @Bean
  public SpringMessageResolver springMessageResolver(MessageSource messageSource) {
    SpringMessageResolver resolver = new SpringMessageResolver();
    resolver.setMessageSource(messageSource);
    return resolver;
  }
  
}
