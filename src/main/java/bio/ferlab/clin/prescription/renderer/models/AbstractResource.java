package bio.ferlab.clin.prescription.renderer.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractResource {

  public static final String IS_PROBAND = "http://fhir.cqgc.ferlab.bio/StructureDefinition/is-proband";
  public static final String IS_FETUS = "http://fhir.cqgc.ferlab.bio/StructureDefinition/is-fetus";
  public static final String FAMILY_ID = "http://fhir.cqgc.ferlab.bio/StructureDefinition/family-id";

  private String resourceType;
  private String id;
  private Meta meta;
  private List<Extension> extension = new ArrayList<>();
  private List<Identifier> identifier = new ArrayList<>();
  
  protected Optional<Extension> getExtension(String name) {
    return extension.stream().filter(e -> name.equals(e.getUrl())).findFirst();
  }

  protected Optional<Identifier> getIdentifier(String code) {
    return identifier.stream().filter(i -> i.getType().getCoding().stream().anyMatch(c -> code.equals(c.getCode()))).findFirst();
  }
}
