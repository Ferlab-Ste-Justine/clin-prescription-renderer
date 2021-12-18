package bio.ferlab.clin.prescription.renderer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient extends AbstractResource {

  private List<Name> name = new ArrayList<>();
  private String birthDate;
  private String gender;

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
