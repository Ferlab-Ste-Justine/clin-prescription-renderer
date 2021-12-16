package bio.ferlab.clin.prescription.renderer.controllers;

import bio.ferlab.clin.prescription.renderer.clients.ServiceRequestAsyncClient;
import bio.ferlab.clin.prescription.renderer.clients.ServiceRequestClient;
import bio.ferlab.clin.prescription.renderer.exceptions.RenderException;
import bio.ferlab.clin.prescription.renderer.models.Bundle;
import bio.ferlab.clin.prescription.renderer.models.Resource;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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
  private ServiceRequestAsyncClient serviceRequestAsyncClient;

  @Autowired
  private SecurityService securityService;

  @Autowired
  private ResourceService resourceService;
  
  @RequestMapping("/render/{serviceRequestId}")
  public String render(final Model model, 
                       @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization,
                       @PathVariable String serviceRequestId,
                       @RequestParam(defaultValue = "fr") String lang) throws ExecutionException, InterruptedException {
    
    final Bundle bundle = serviceRequestClient.getBundleById(authorization, serviceRequestId);
    
    final Resource serviceRequest = bundle.findByResourceType(Bundle.ResourceType.ServiceRequest)
        .orElseThrow(() -> new RenderException(HttpStatus.NOT_FOUND, "Service request: " + serviceRequestId));
    final Resource patient = bundle.findByResourceType(Bundle.ResourceType.Patient)
        .orElseThrow(() -> new RenderException(HttpStatus.NOT_FOUND, "Patient: " + serviceRequest.getSubject().getId()));
    final Resource group = Optional.ofNullable(serviceRequestClient.getGroupById(authorization, patient.getFamilyId()))
        .orElseThrow(() -> new RenderException(HttpStatus.NOT_FOUND, "Group: " + patient.getFamilyId()));
    
    final List<CompletableFuture<Resource>> membersFutures = group.getMember().stream()
        .map(grp -> grp.getEntity().getId()).map(id -> serviceRequestAsyncClient.getPatientById(authorization, id)).collect(Collectors.toList());
    final List<Resource> members = membersFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
    
    model.addAttribute("serviceRequest", serviceRequest);
    model.addAttribute("patient", patient);
    model.addAttribute("group", group);
    model.addAttribute("members", members);

    return "render";
  }

}
