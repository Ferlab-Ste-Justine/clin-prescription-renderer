package bio.ferlab.clin.prescription.renderer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Resource {
  
  public static final String IS_PROBAND = "http://fhir.cqgc.ferlab.bio/StructureDefinition/is-proband";
  public static final String IS_FETUS = "http://fhir.cqgc.ferlab.bio/StructureDefinition/is-fetus";
  public static final String FAMILY_ID = "http://fhir.cqgc.ferlab.bio/StructureDefinition/family-id";
  
  private String resourceType;
  private String id;
  private Meta meta;
  private Code code;
  private Date birthDate;
  private Reference subject;
  private List<Extension> extension = new ArrayList<>();
  private List<Member> member = new ArrayList<>();
  
  public boolean isProband() {
    return getExtension(IS_PROBAND).map(Extension::getValueBoolean).orElse(false);
  }

  public boolean isFetus() {
    return getExtension(IS_FETUS).map(Extension::getValueBoolean).orElse(false);
  }

  public String getFamilyId() {
    return getExtension(FAMILY_ID).map(e -> e.getValueReference().getId()).orElse(null);
  }

  private Optional<Extension> getExtension(String name) {
    return extension.stream().filter(e -> name.equals(e.getUrl())).findFirst();
  }
  
}
