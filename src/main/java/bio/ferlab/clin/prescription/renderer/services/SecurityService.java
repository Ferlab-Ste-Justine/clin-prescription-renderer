package bio.ferlab.clin.prescription.renderer.services;

import bio.ferlab.clin.prescription.renderer.configurations.SecurityConfiguration;
import bio.ferlab.clin.prescription.renderer.exceptions.SecurityException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service
public class SecurityService {
  
  @Autowired
  private SecurityConfiguration configuration;
  
  public void checkAuthorization(String authorization) {
    if(configuration.isEnabled()) {
      if (!StringUtils.hasText(authorization)) {
        throw new SecurityException(HttpStatus.UNAUTHORIZED, "missing token");
      }
      try {
        final String token = authorization.replace("Bearer ", "");
        DecodedJWT jwt = JWT.decode(token);
        if (jwt.getExpiresAt().before(new Date())) {
          throw new SecurityException(HttpStatus.FORBIDDEN, "token expired");
        }
        if (!jwt.getAudience().get(0).equals(configuration.getAudience())) {
          throw new SecurityException(HttpStatus.FORBIDDEN, "invalid audience");
        }
        if (!jwt.getIssuer().equals(configuration.getIssuer())){
          throw new SecurityException(HttpStatus.FORBIDDEN, "invalid issuer");
        }
      } catch(JWTDecodeException e) {
        throw new SecurityException(HttpStatus.FORBIDDEN, "invalid token");
      }
    }
  }
}
