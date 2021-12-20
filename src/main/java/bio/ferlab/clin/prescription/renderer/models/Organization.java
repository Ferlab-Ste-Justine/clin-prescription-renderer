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
public class Organization extends AbstractResource {
  private String name;
  private List<String> alias = new ArrayList<>();
  private List<Contact> contact = new ArrayList<>();

  public String getPhone() {
    return contact.get(0).getTelecom().stream().filter(t -> "phone".equals(t.getSystem())).findFirst().map(Telecom::getValue).orElse(null);
  }

  public String getFax() {
    return contact.get(0).getTelecom().stream().filter(t -> "fax".equals(t.getSystem())).findFirst().map(Telecom::getValue).orElse(null);
  }

  public String getAddress() {
    return contact.get(0).getAddress().getText();
  }
}
