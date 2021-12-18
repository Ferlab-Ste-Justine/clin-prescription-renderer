package bio.ferlab.clin.prescription.renderer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceRequest extends AbstractResource {

  private Code code;
  private Reference subject;

  public String getDossier() {
    return getIdentifier("MR").map(Identifier::getValue).orElse(null);
  }
  
  public String getOrganizationId() {
    return getIdentifier("MR").map(i -> i.getAssigner().getId()).orElse(null);
  }
}
