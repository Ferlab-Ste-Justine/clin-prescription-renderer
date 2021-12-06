package bio.ferlab.clin.prescription.renderer.controllers;

import bio.ferlab.clin.prescription.renderer.clients.ServiceRequestClient;
import bio.ferlab.clin.prescription.renderer.models.ServiceRequest;
import bio.ferlab.clin.prescription.renderer.services.PdfService;
import bio.ferlab.clin.prescription.renderer.services.ResourceService;
import bio.ferlab.clin.prescription.renderer.services.SecurityService;
import bio.ferlab.clin.prescription.renderer.services.ThymeleafService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.net.URISyntaxException;

@Profile("dev")
@Controller
public class DevController {

  @Autowired
  private ThymeleafService thymeleafService;

  @Autowired
  private PdfService pdfService;

  @Autowired
  private ServiceRequestClient serviceRequestClient;

  @Autowired
  private SecurityService securityService;

  @Autowired
  private ResourceService resourceService;
  
  @RequestMapping("/render/{serviceRequestId}")
  public String render(final Model model, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization ,@PathVariable String serviceRequestId) throws URISyntaxException, IOException {

    final ServiceRequest serviceRequest = serviceRequestClient.getById(authorization, serviceRequestId);

    model.addAttribute("serviceRequest", serviceRequest);
    model.addAttribute("chu", resourceService.getImageBase64("img/chu.png"));

    return "render";
  }

}
