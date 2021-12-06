package bio.ferlab.clin.prescription.renderer.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceRequest {
  
  private String resourceType;
  private String id;
  private Meta meta;
}
