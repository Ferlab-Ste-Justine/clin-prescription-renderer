package bio.ferlab.clin.prescription.renderer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RenderException extends ResponseStatusException {

  public RenderException(HttpStatus status, String reason) {
    super(status, reason);
  }
}
