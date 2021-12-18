package bio.ferlab.clin.prescription.renderer.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

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
  
  private Optional<Resource> findByResourceType(ResourceType type) {
    return getEntry().stream().map(Entry::getResource)
        .filter(r -> type.name().equals(r.getResourceType()))
        .findFirst();
  }

  public Optional<ServiceRequest> findServiceRequest() {
    return findByResourceType(ResourceType.ServiceRequest).map(r -> {
      ServiceRequest result = new ServiceRequest();
      BeanUtils.copyProperties(r, result);
      return result;
    });
  }
  
  public Optional<Patient> findPatient() {
    return findByResourceType(ResourceType.Patient).map(r -> {
      Patient result = new Patient();
      BeanUtils.copyProperties(r, result);
      return result;
    });
  }
}
