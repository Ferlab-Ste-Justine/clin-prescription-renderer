package bio.ferlab.clin.prescription.renderer.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Meta {
  private Date lastUpdated;
}
