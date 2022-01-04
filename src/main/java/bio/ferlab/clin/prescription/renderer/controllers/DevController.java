package bio.ferlab.clin.prescription.renderer.controllers;

import bio.ferlab.clin.prescription.renderer.clients.FhirAsyncClient;
import bio.ferlab.clin.prescription.renderer.clients.FhirClient;
import bio.ferlab.clin.prescription.renderer.exceptions.RenderException;
import bio.ferlab.clin.prescription.renderer.models.*;
import bio.ferlab.clin.prescription.renderer.services.PdfService;
import bio.ferlab.clin.prescription.renderer.services.ResourceService;
import bio.ferlab.clin.prescription.renderer.services.SecurityService;
import bio.ferlab.clin.prescription.renderer.services.ThymeleafService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Profile("dev")
@Controller
public class DevController {
  
  @Autowired
  private PdfController pdfController;
  
  @RequestMapping("/render/{serviceRequestId}")
  public String render(final Model model, 
                       @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization,
                       @PathVariable String serviceRequestId) {
    
    final Map<String, Object> params = pdfController.getDataFromFhir(authorization, serviceRequestId);
    
    model.addAllAttributes(params);
    // flying saucer flexbox marche pas
    // 1102
    // 3604
    // 3606
    // 3609
    // 3611
    return params.get(PdfController.TEMPLATE).toString();
  }

}
