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
public class Bundle<T extends AbstractResource> extends AbstractResource {
  
  private List<Entry<T>> entry = new ArrayList<>();

  public Optional<T> getFirst() {
    return entry.stream().findFirst().map(Entry::getResource);
  }

}
