package bio.ferlab.clin.prescription.renderer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bundle {
  
  public enum ResourceType {
    ServiceRequest,
    Patient
  }
  
  private List<Entry> entry = new ArrayList<>();
  
  public Optional<Resource> findByResourceType(ResourceType resourceType) {
    return getEntry().stream().map(Entry::getResource)
        .filter(resource -> resourceType.name().equals(resource.getResourceType()))
        .findFirst();
  }
}
