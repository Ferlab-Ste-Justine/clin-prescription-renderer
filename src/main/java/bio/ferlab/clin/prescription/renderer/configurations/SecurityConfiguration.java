package bio.ferlab.clin.prescription.renderer.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("security")
@Data
public class SecurityConfiguration {
  
  private boolean enabled;
  private String audience;
  private String issuer;
  
}
