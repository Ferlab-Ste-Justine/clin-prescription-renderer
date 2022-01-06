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
public class Practitioner extends AbstractResource {
  
  private List<Name> name = new ArrayList<>();
  
  public String getFormattedName() {
    return name.stream().findFirst().map(n -> 
      String.format("%s %s, %s - %s", 
          n.getPrefix().stream().findFirst().orElse(""), 
          n.getFamily().toUpperCase(), 
          n.getGiven().stream().findFirst().orElse(""),
          getIdentifier("MD").map(Identifier::getValue).orElse(""))
    ).orElse(null);
  }
}
