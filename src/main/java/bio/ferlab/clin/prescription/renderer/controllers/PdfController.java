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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Controller
public class PdfController {
  
  public static final String TEMPLATE = "template";
  
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
                                      @RequestParam(required = false) String lang,
                                      @PathVariable String serviceRequestId) {
    
    this.securityService.checkAuthorization(authorization);

    final Map<String, Object> params = getDataFromFhir(authorization, serviceRequestId);
    
    final Locale locale = StringUtils.parseLocaleString(lang);
        
    String template = thymeleafService.parseTemplate(params.get(TEMPLATE).toString(), params, locale);
    byte[] pdf = pdfService.generateFromHtml(template);
    ByteArrayResource resource = new ByteArrayResource(pdf);

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + serviceRequestId + ".pdf")
        .contentLength(resource.contentLength())
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(resource);
  }
  
  public Map<String, Object> getDataFromFhir(String authorization, String serviceRequestId) {

    final ServiceRequest serviceRequest = fhirClient.getServiceRequestById(authorization, serviceRequestId);
    final Patient patient = fhirClient.getPatientById(authorization, serviceRequest.getSubject().getId());

    List<Patient> members = new ArrayList<>();
    Organization organization;
    Practitioner requester;
    Practitioner supervisor;
    PractitionerRole requesterRole;
    Organization performer;
    Patient mother;

    Optional<CompletableFuture<Group>> groupFuture = Optional.empty();
    Optional<CompletableFuture<Patient>> motherFuture = Optional.empty();

    if (patient.getFamilyId() != null) {
      groupFuture = Optional.of(serviceRequestAsyncClient.getGroupById(authorization, patient.getFamilyId()));
    }
    
    if (patient.isFetus()) {
      motherFuture =  Optional.of(serviceRequestAsyncClient.getPatientById(authorization, patient.getMotherId()));
    }
    
    final CompletableFuture<Organization> organizationFuture = serviceRequestAsyncClient.getOrganizationById(authorization, serviceRequest.getOrganizationId());
    final CompletableFuture<Organization> performerFuture = serviceRequestAsyncClient.getOrganizationById(authorization, serviceRequest.getPerformerId());
    final CompletableFuture<Practitioner> requesterFuture = serviceRequestAsyncClient.getPractitionerById(authorization, serviceRequest.getRequester().getId());
    final CompletableFuture<Practitioner> supervisorFuture = serviceRequest.getSupervisorId().map(id -> serviceRequestAsyncClient.getPractitionerById(authorization, id))
        .orElseGet(() -> CompletableFuture.completedFuture(null));
    final CompletableFuture<Bundle<PractitionerRole>> requesterRoleFuture = serviceRequestAsyncClient.getPractitionerRole(authorization, serviceRequest.getRequester().getId(), serviceRequest.getOrganizationId());

    organization = organizationFuture.join();
    performer = performerFuture.join();
    requester = requesterFuture.join();
    supervisor = supervisorFuture.join();
    requesterRole = requesterRoleFuture.join().getFirst()
        .orElseThrow(() -> new RenderException(HttpStatus.NOT_FOUND, "Requester role: " + serviceRequest.getRequester().getId()));
    mother = motherFuture.map(CompletableFuture::join).orElse(null);
    groupFuture.map(CompletableFuture::join).ifPresent(grp -> {
      final List<CompletableFuture<Patient>> membersFutures = grp.getMember().stream()
          .map(member -> member.getEntity().getId()).map(id -> serviceRequestAsyncClient.getPatientById(authorization, id)).collect(Collectors.toList());
      members.addAll(membersFutures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
    });
    
    final Map<String, Object> params = new HashMap<>();
    params.put("serviceRequest", serviceRequest);
    params.put("patient", patient);
    params.put("organization", organization);
    params.put("members", members);
    params.put("requester", requester);
    params.put("supervisor", supervisor);
    params.put("requesterRole", requesterRole);
    params.put("performer", performer);

    final Patient proband = members.stream().filter(Patient::isProband).findFirst().orElse(null);
    
    final boolean isIndex = patient.isProband() && !patient.isFetus() && members.size() < 2;
    final boolean isIndexWithFamily = patient.isProband() && !patient.isFetus() && members.size() > 1;
    final boolean isParentOfIndex = !patient.isProband() && proband != null && !proband.isFetus();
    final boolean isParentOfFetus = !patient.isProband() && proband != null && proband.isFetus();
    final boolean isFetus = patient.isFetus();
    
    if (isIndex) {
      params.put(TEMPLATE, "index");
    } else if (isIndexWithFamily) {
      params.put(TEMPLATE, "indexWithFamily");
    } else if (isParentOfIndex) {
      params.put(TEMPLATE, "parentOfIndex");
      params.put("proband", proband);
    } else if (isParentOfFetus) {
      params.put(TEMPLATE, "parentOfFetus");
      params.put("fetus", proband);
    } else if (isFetus) {
      params.put(TEMPLATE, "fetus");
      params.put("patient", mother);  // replace patient with mother
      params.put("fetus", patient);
    } else {
      throw new RenderException(HttpStatus.NOT_IMPLEMENTED, "Unknown type of template: " + serviceRequestId);
    }

    params.put("base64Checkbox", resourceService.getImageBase64("checkbox.gif"));
    
    return params;
  }
}
