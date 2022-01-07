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
public class Contact {
  private List<Telecom> telecom = new ArrayList<>();
  private Address address;
  
  public String getTelecom(String system) {
    return telecom.stream().filter(t -> system.equals(t.getSystem())).map(Telecom::getValue).findFirst().orElse(null);
  }
}
