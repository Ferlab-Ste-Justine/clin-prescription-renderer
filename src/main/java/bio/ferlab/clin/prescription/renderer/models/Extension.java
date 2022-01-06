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
public class Extension {
 
  private String url;
  private Boolean valueBoolean;
  private Reference valueReference;
  private ValueCoding valueCoding;
  private List<Extension> extension = new ArrayList<>();
  private ValueCodeableConcept valueCodeableConcept;
}
