package bio.ferlab.clin.prescription.renderer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization extends AbstractResource {
  private String name;
  private List<String> alias = new ArrayList<>();
  private List<Contact> contact = new ArrayList<>();
  
  private String getContact(String system) {
    return contact.stream().map(c -> c.getTelecom(system)).filter(Objects::nonNull).findFirst().orElse(null);
  }

  public String getPhone() {
    return getContact("phone");
  }

  public String getFax() {
    return getContact("fax");
  }

  public String getAddress() {
    return contact.stream().filter(c -> c.getAddress() != null).map(c -> c.getAddress().getText()).findFirst().orElse(null);
  }
}
