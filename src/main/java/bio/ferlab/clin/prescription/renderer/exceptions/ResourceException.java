package bio.ferlab.clin.prescription.renderer.exceptions;

public class ResourceException extends RuntimeException {
  
  public ResourceException(Exception e) {
    super(e);
  }
}
