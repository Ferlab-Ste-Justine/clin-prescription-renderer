package bio.ferlab.clin.prescription.renderer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Profile("dev")
@Controller
public class DevController {

  static {
    System.setProperty("javax.net.ssl.trustStore", "cacerts.jks");
    System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
  }
  
  @Autowired
  private PdfController pdfController;
  
  @RequestMapping("/render/{serviceRequestId}")
  public String render(final Model model, 
                       @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization,
                       @PathVariable String serviceRequestId) {
    
    final Map<String, Object> params = pdfController.getDataFromFhir(authorization, serviceRequestId);
    
    model.addAllAttributes(params);
    
    return params.get(PdfController.TEMPLATE).toString();
  }

}
