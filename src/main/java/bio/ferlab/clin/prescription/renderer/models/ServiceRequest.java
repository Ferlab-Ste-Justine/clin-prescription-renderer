package bio.ferlab.clin.prescription.renderer.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ServiceRequest {
  
  private String resourceType;
  private String id;
  private Meta meta;
}
