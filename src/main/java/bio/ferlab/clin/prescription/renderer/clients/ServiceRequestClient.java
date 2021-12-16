package bio.ferlab.clin.prescription.renderer.clients;

import bio.ferlab.clin.prescription.renderer.models.Bundle;
import bio.ferlab.clin.prescription.renderer.models.Resource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "ServiceRequestClient", url = "${feign.client.url.fhirServer}")
public interface ServiceRequestClient {

  @RequestMapping(method = RequestMethod.GET, value = "/ServiceRequest/{id}", produces = "application/json")
  Resource getById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @PathVariable String id);

  @RequestMapping(method = RequestMethod.GET, value = "/ServiceRequest?_id={id}&_include=ServiceRequest:patient", produces = "application/json")
  Bundle getBundleById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @PathVariable String id);

  @RequestMapping(method = RequestMethod.GET, value = "/Group/{id}", produces = "application/json")
  Resource getGroupById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @PathVariable String id);

  @RequestMapping(method = RequestMethod.GET, value = "/Patient/{id}", produces = "application/json")
  Resource getPatientById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @PathVariable String id);

}
