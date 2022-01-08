package bio.ferlab.clin.prescription.renderer.configurations;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.spring5.messageresolver.SpringMessageResolver;

import java.util.Locale;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  
  @Bean
  public LocaleResolver localeResolver() {
    SessionLocaleResolver slr = new SessionLocaleResolver();
    slr.setDefaultLocale(Locale.FRENCH);  // it's not a mistake ... FR by default
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
        .allowedOrigins("http://localhost:2000", "https://qa.clin.ferlab.bio", "https://staging.clin.ferlab.bio", "https://clin.ferlab.bio")
        .allowedMethods("GET")
        .allowedHeaders("Authorization")
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
    return messageSource;
  }
  
  @Bean
  public SpringMessageResolver springMessageResolver(MessageSource messageSource) {
    SpringMessageResolver resolver = new SpringMessageResolver();
    resolver.setMessageSource(messageSource);
    return resolver;
  }
  
}
