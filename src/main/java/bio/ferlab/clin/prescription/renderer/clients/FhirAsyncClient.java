package bio.ferlab.clin.prescription.renderer.clients;

import bio.ferlab.clin.prescription.renderer.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
public class FhirAsyncClient {
  
  @Autowired
  @Qualifier("feignExecutorService")
  private ExecutorService es;
  
  @Autowired
  private FhirClient client;

  public CompletableFuture<Patient> getPatientById(String authorization, String id) {
    return CompletableFuture.supplyAsync(() -> client.getPatientById(authorization, id), es);
  }

  public CompletableFuture<Group> getGroupById(String authorization, String id) {
    return CompletableFuture.supplyAsync(() -> client.getGroupById(authorization, id), es);
  }
  
  public CompletableFuture<Organization> getOrganizationById(String authorization, String id) {
    return CompletableFuture.supplyAsync(() -> client.getOrganizationById(authorization, id), es);
  }

  public CompletableFuture<Practitioner> getPractitionerById(String authorization, String id) {
    return CompletableFuture.supplyAsync(() -> client.getPractitionerById(authorization, id), es);
  }

  public CompletableFuture<Bundle<PractitionerRole>> getPractitionerRole(String authorization, String practitionerId, String organizationId) {
    return CompletableFuture.supplyAsync(() -> client.getPractitionerRole(authorization, practitionerId, organizationId), es);
  }

}
