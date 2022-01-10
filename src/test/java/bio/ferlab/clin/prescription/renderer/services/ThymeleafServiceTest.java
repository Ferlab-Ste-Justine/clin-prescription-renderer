package bio.ferlab.clin.prescription.renderer.services;

import bio.ferlab.clin.prescription.renderer.models.Meta;
import bio.ferlab.clin.prescription.renderer.models.ServiceRequest;
import org.junit.jupiter.api.Test;
import org.thymeleaf.messageresolver.StandardMessageResolver;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ThymeleafServiceTest {
  
  private final ThymeleafService service = new ThymeleafService(new StandardMessageResolver());
  
  @Test
  void generateTemplate() {
    final ServiceRequest serviceRequest = new ServiceRequest();
    serviceRequest.setId("123");
    serviceRequest.setResourceType("ServiceRequest");
    serviceRequest.setMeta(new Meta());
    
    final Map<String, Object> params = new HashMap<>();
    params.put("image", "imagebase64data");
    params.put("serviceRequest", serviceRequest);

    String template = service.parseTemplate("test",params, null);
    assertTrue(template.contains("<h1>Prescription</h1>"));
    assertTrue(template.contains("<img src=\"imagebase64data\" />"));
    assertTrue(template.contains("Id: <p>123</p>"));
    assertTrue(template.contains("Type: <p>ServiceRequest</p>"));
  }

}