package bio.ferlab.clin.prescription.renderer.services;

import bio.ferlab.clin.prescription.renderer.configurations.SecurityConfiguration;
import bio.ferlab.clin.prescription.renderer.exceptions.SecurityException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class SecurityServiceTest {
  
  private final Algorithm algorithm = Algorithm.HMAC256("secret");
  private final SecurityConfiguration configuration = Mockito.mock(SecurityConfiguration.class);
  private final SecurityService service = new SecurityService(configuration);
  
  @BeforeEach
  void before() {
    when(configuration.isEnabled()).thenReturn(true);
    when(configuration.getAudience()).thenReturn("123");
    when(configuration.getIssuer()).thenReturn("http://abc");
  }
  
  @Test
  void missingToken() {
    SecurityException exception = assertThrows(SecurityException.class, () -> {
      service.checkAuthorization(null);
    });
    assertEquals("missing token", exception.getReason());
  }

  @Test
  void invalidToken() {
    SecurityException exception = assertThrows(SecurityException.class, () -> {
      service.checkAuthorization("yolotoken");
    });
    assertEquals("invalid token", exception.getReason());
  }

  @Test
  void expiredToken() {
    final Date expire = Date.from(LocalDateTime.now().minusMinutes(5).atZone(ZoneId.systemDefault()).toInstant());
    final String token = JWT.create()
        .withExpiresAt(expire)
        .sign(algorithm);
    SecurityException exception = assertThrows(SecurityException.class, () -> {
      service.checkAuthorization(token);
    });
    assertEquals("token expired", exception.getReason());
  }

  @Test
  void badAudienceToken() {
    final Date expire = Date.from(LocalDateTime.now().plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant());
    final String token = JWT.create()
        .withExpiresAt(expire)
        .withAudience("foo")
        .sign(algorithm);
    SecurityException exception = assertThrows(SecurityException.class, () -> {
      service.checkAuthorization(token);
    });
    assertEquals("invalid audience", exception.getReason());
  }

  @Test
  void badIssuerToken() {
    final Date expire = Date.from(LocalDateTime.now().plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant());
    final String token = JWT.create()
        .withExpiresAt(expire)
        .withAudience("123")
        .withIssuer("bar")
        .sign(algorithm);
    SecurityException exception = assertThrows(SecurityException.class, () -> {
      service.checkAuthorization(token);
    });
    assertEquals("invalid issuer", exception.getReason());
  }

}