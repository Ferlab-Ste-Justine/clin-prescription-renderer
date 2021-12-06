package bio.ferlab.clin.prescription.renderer.clients;

import bio.ferlab.clin.prescription.renderer.models.ServiceRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "ServiceRequestClient", url = "${feign.client.url.fhirServer}")
public interface ServiceRequestClient {

  @RequestMapping(method = RequestMethod.GET, value = "/ServiceRequest/{id}", produces = "application/json")
  ServiceRequest getById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @PathVariable("id") String postId);
  
}
