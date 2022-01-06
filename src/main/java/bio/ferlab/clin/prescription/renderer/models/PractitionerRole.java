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
public class PractitionerRole extends AbstractResource {
  
  private List<Telecom> telecom = new ArrayList<>();
  
  public String getPhone() {
    return telecom.stream().filter(t -> "phone".equals(t.getSystem())).findFirst().map(Telecom::getValue).orElse(null);
  }
  
  public String getEmail() {
    return telecom.stream().filter(t -> "email".equals(t.getSystem())).findFirst().map(Telecom::getValue).orElse(null);
  }
}
