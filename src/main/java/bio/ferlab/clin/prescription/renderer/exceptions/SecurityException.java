package bio.ferlab.clin.prescription.renderer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SecurityException extends ResponseStatusException {

  public SecurityException(HttpStatus status, String reason) {
    super(status, reason);
  }
}
