package bio.ferlab.clin.prescription.renderer.services;

import bio.ferlab.clin.prescription.renderer.models.Meta;
import bio.ferlab.clin.prescription.renderer.models.ServiceRequest;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ThymeleafServiceTest {
  
  private final ThymeleafService service = new ThymeleafService();
  
  @Test
  void generateTemplate() {
    final ServiceRequest serviceRequest = ServiceRequest.builder()
        .id("123")
        .resourceType("ServiceRequest")
        .meta(Meta.builder().build())
        .build();
    
    final Map<String, Object> params = new HashMap<>();
    params.put("serviceRequest", serviceRequest);

    String template = service.parseTemplate("test",params);
    assertTrue(template.contains("<h1>Prescription</h1>"));
    assertTrue(template.contains("Id: <p>123</p>"));
    assertTrue(template.contains("Type: <p>ServiceRequest</p>"));
  }

}