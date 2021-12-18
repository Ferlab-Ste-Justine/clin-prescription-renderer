package bio.ferlab.clin.prescription.renderer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Resource extends AbstractResource {
  
  private Code code;
  private String birthDate;
  private String gender;
  private Reference subject;
  private List<String> alias = new ArrayList<>();
  private List<Name> name = new ArrayList<>();
  
}
