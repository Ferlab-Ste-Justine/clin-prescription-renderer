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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Controller
public class PdfController {
  
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
  
  @RequestMapping("/pdf/{serviceRequestId}")
  public ResponseEntity<org.springframework.core.io.Resource> pdf(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                      @PathVariable String serviceRequestId) {
    
    this.securityService.checkAuthorization(authorization);

    final ServiceRequest serviceRequest = fhirClient.getServiceRequestById(authorization, serviceRequestId);
    final Patient patient = fhirClient.getPatientById(authorization, serviceRequest.getSubject().getId());

    List<Patient> members = new ArrayList<>();
    Organization organization = null;
    Practitioner requester = null;
    Practitioner supervisor = null;
    PractitionerRole requesterRole = null;
    Organization performer = null;

    if (patient.getFamilyId() != null) {
      // async calls
      final CompletableFuture<Group> groupFuture = serviceRequestAsyncClient.getGroupById(authorization, patient.getFamilyId());
      final CompletableFuture<Organization> organizationFuture = serviceRequestAsyncClient.getOrganizationById(authorization, serviceRequest.getOrganizationId());
      final CompletableFuture<Organization> performerFuture = serviceRequestAsyncClient.getOrganizationById(authorization, serviceRequest.getPerformerId());
      final CompletableFuture<Practitioner> requesterFuture = serviceRequestAsyncClient.getPractitionerById(authorization, serviceRequest.getRequester().getId());
      final CompletableFuture<Practitioner> supervisorFuture = serviceRequest.getSupervisorId().map(id -> serviceRequestAsyncClient.getPractitionerById(authorization, id))
          .orElseGet(() -> CompletableFuture.completedFuture(null));
      final CompletableFuture<Bundle<PractitionerRole>> requesterRoleFuture = serviceRequestAsyncClient.getPractitionerRole(authorization, serviceRequest.getRequester().getId(), serviceRequest.getOrganizationId());
      final List<CompletableFuture<Patient>> membersFutures = groupFuture.join().getMember().stream()
          .map(grp -> grp.getEntity().getId()).map(id -> serviceRequestAsyncClient.getPatientById(authorization, id)).collect(Collectors.toList());
      // async join
      members.addAll(membersFutures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
      organization = organizationFuture.join();
      performer = performerFuture.join();
      requester = requesterFuture.join();
      supervisor = supervisorFuture.join();
      requesterRole = requesterRoleFuture.join().getFirst()
          .orElseThrow(() -> new RenderException(HttpStatus.NOT_FOUND, "Requester role: " + serviceRequest.getRequester().getId()));
    }

    final Map<String, Object> params = new HashMap<>();
    params.put("serviceRequest", serviceRequest);
    params.put("patient", patient);
    params.put("organization", organization);
    params.put("members", members);
    params.put("requester", requester);
    params.put("supervisor", supervisor);
    params.put("requesterRole", requesterRole);
    params.put("performer", performer);


    String template = thymeleafService.parseTemplate("render",params);
    byte[] pdf = pdfService.generateFromHtml(template);
    ByteArrayResource resource = new ByteArrayResource(pdf);

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + UUID.randomUUID() + ".pdf")
        .contentLength(resource.contentLength())
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(resource);
  }
}
