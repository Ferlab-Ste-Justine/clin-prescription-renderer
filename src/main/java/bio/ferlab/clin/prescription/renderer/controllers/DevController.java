package bio.ferlab.clin.prescription.renderer.controllers;

import bio.ferlab.clin.prescription.renderer.clients.FhirAsyncClient;
import bio.ferlab.clin.prescription.renderer.clients.FhirClient;
import bio.ferlab.clin.prescription.renderer.models.Group;
import bio.ferlab.clin.prescription.renderer.models.Organization;
import bio.ferlab.clin.prescription.renderer.models.Patient;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Profile("dev")
@Controller
public class DevController {

  @Autowired
  private ThymeleafService thymeleafService;

  @Autowired
  private PdfService pdfService;

  @Autowired
  private FhirClient fhirClient;
  
  @Autowired
  private FhirAsyncClient serviceRequestAsyncClient;

  @Autowired
  private SecurityService securityService;

  @Autowired
  private ResourceService resourceService;
  
  @RequestMapping("/render/{serviceRequestId}")
  public String render(final Model model, 
                       @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization,
                       @PathVariable String serviceRequestId) {
    
    /*final Bundle bundle = fhirClient.getBundleById(authorization, serviceRequestId);
    
    final ServiceRequest serviceRequest = bundle.findServiceRequest()
        .orElseThrow(() -> new RenderException(HttpStatus.NOT_FOUND, "Service request: " + serviceRequestId));
    final Patient patient = bundle.findPatient()
        .orElseThrow(() -> new RenderException(HttpStatus.NOT_FOUND, "Patient: " + serviceRequest.getSubject().getId()));*/
    
    final ServiceRequest serviceRequest = fhirClient.getServiceRequestById(authorization, serviceRequestId);
    final Patient patient = fhirClient.getPatientById(authorization, serviceRequest.getSubject().getId());
    
    final List<Patient> members = new ArrayList<>();
    Organization organization = null;
    if (patient.getFamilyId() != null) {
      // async calls
      final Group group = fhirClient.getGroupById(authorization, patient.getFamilyId());
      final CompletableFuture<Organization> organizationFuture = serviceRequestAsyncClient.getOrganizationById(authorization, serviceRequest.getOrganizationId());
      // async join
      final List<CompletableFuture<Patient>> membersFutures = group.getMember().stream()
          .map(grp -> grp.getEntity().getId()).map(id -> serviceRequestAsyncClient.getPatientById(authorization, id)).collect(Collectors.toList());
      members.addAll(membersFutures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
      organization = organizationFuture.join();
    }
    
    model.addAttribute("serviceRequest", serviceRequest);
    model.addAttribute("patient", patient);
    model.addAttribute("organization", organization);
    model.addAttribute("members", members);
    
    final Patient proband = members.stream().filter(Patient::isProband).findFirst().orElse(null);
    
    final boolean isProbandWithoutFamily = patient.isProband() && !patient.isFetus() && members.size() < 2;

    return "render";
  }

}
