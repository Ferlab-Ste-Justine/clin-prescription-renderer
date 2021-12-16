package bio.ferlab.clin.prescription.renderer.controllers;

import bio.ferlab.clin.prescription.renderer.clients.ServiceRequestClient;
import bio.ferlab.clin.prescription.renderer.models.Resource;
import bio.ferlab.clin.prescription.renderer.services.PdfService;
import bio.ferlab.clin.prescription.renderer.services.ResourceService;
import bio.ferlab.clin.prescription.renderer.services.SecurityService;
import bio.ferlab.clin.prescription.renderer.services.ThymeleafService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class PdfController {
  
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
  
  @RequestMapping("/pdf/{serviceRequestId}")
  public ResponseEntity<org.springframework.core.io.Resource> pdf(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                      @PathVariable String serviceRequestId,
                                      @RequestParam(defaultValue = "fr") String lang) {
    
    this.securityService.checkAuthorization(authorization);
    
    final Resource serviceRequest = serviceRequestClient.getById(authorization, serviceRequestId);
    
    final Map<String, Object> params = new HashMap<>();
    params.put("serviceRequest", serviceRequest);
    params.put("chu", resourceService.getImageBase64("img/chu.png"));
    
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
