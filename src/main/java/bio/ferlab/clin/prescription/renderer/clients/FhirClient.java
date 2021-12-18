package bio.ferlab.clin.prescription.renderer.clients;

import bio.ferlab.clin.prescription.renderer.models.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "FhirClient", url = "${feign.client.url.fhirServer}")
public interface FhirClient {

  @RequestMapping(method = RequestMethod.GET, value = "/ServiceRequest/{id}", produces = "application/json")
  ServiceRequest getServiceRequestById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @PathVariable String id);

  @RequestMapping(method = RequestMethod.GET, value = "/ServiceRequest?_id={id}&_include=ServiceRequest:patient", produces = "application/json")
  Bundle getBundleById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @PathVariable String id);

  @RequestMapping(method = RequestMethod.GET, value = "/Group/{id}", produces = "application/json")
  Group getGroupById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @PathVariable String id);

  @RequestMapping(method = RequestMethod.GET, value = "/Patient/{id}", produces = "application/json")
  Patient getPatientById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @PathVariable String id);

  @RequestMapping(method = RequestMethod.GET, value = "/Organization/{id}", produces = "application/json")
  Organization getOrganizationById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization, @PathVariable String id);
  
}
