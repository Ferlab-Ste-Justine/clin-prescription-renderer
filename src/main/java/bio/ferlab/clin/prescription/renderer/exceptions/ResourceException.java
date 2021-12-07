package bio.ferlab.clin.prescription.renderer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ResourceException extends RuntimeException {
  
  public ResourceException(String message, Exception e) {
    super(message, e);
  }
}
