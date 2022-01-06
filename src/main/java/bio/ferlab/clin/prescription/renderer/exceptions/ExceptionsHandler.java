package bio.ferlab.clin.prescription.renderer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@RestControllerAdvice
public class ExceptionsHandler {

  @ExceptionHandler(SecurityException.class)
  public ResponseEntity<String> securityException(SecurityException e) {
    final HttpStatus status = Optional.ofNullable(e.getStatus()).orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    return ResponseEntity.status(status).body(e.getReason());
  }
  
}
