package bio.ferlab.clin.prescription.renderer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceRequest extends AbstractResource {

  private Code code;
  private Reference subject;
  private Reference requester;
  private List<Reference> performer;

  public String getDossier() {
    return getIdentifier("MR").map(Identifier::getValue).orElse(null);
  }
  
  public String getPerformerId() {
    return performer.stream().findFirst().map(Reference::getId).orElse(null);
  }
  
  public String getOrganizationId() {
    return getIdentifier("MR").map(i -> i.getAssigner().getId()).orElse(null);
  }

  public Optional<String> getSupervisorId() {
    return getExtension(RESIDENT_SUPERVISOR).map(e -> e.getValueReference().getId());
  }
}
