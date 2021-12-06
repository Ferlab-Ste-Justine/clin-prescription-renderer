package bio.ferlab.clin.prescription.renderer.models;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Meta {
  private Date lastUpdated;
}
