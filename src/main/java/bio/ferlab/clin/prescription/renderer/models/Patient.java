package bio.ferlab.clin.prescription.renderer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient extends AbstractResource {

  private List<Name> name = new ArrayList<>();
  private String birthDate;
  private String gender;

  public String getDossier() {
    return getIdentifier("MR").map(Identifier::getValue).orElse(null);
  }

  public String getFormattedName() {
    return name.stream().filter(n -> n.getFamily() != null && n.getGiven().size() > 0)
        .findFirst().map(n -> String.format("%s, %s", n.getFamily().toUpperCase(), n.getGiven().get(0))).orElse(null); 
  }
  
  public String getFormattedRamq() {
    return Optional.ofNullable(getRamq())
        .map(r -> String.format("%s %s %s", r.substring(0,4), r.substring(4,8), r.substring(8,12)))
        .orElse(null);
  }
  
  public String getMotherId() {
    String id = null;
    for (Extension ext: getExtension()) {
      if (FAMILY_RELATION.equals(ext.getUrl())) {
        Extension motherRelation = getExtension(ext.getExtension(), "relation")
            .filter(e -> "NMTHF".equals(e.getValueCodeableConcept().getCoding().get(0).getCode())).orElse(null);
        if(motherRelation != null) {
          id = getExtension(ext.getExtension(), "subject").get().getValueReference().getId();
        }
      }
    }
    return id;
  }

  public boolean isProband() {
    return getExtension(IS_PROBAND).map(Extension::getValueBoolean).orElse(false);
  }

  public boolean isFetus() {
    return getExtension(IS_FETUS).map(Extension::getValueBoolean).orElse(false);
  }

  public String getFamilyId() {
    return getExtension(FAMILY_ID).map(e -> e.getValueReference().getId()).orElse(null);
  }

  public String getRamq() {
    return getIdentifier("JHN").map(Identifier::getValue).orElse(null);
  }

}
