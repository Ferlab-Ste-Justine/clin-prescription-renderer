package bio.ferlab.clin.prescription.renderer.clients;

import bio.ferlab.clin.prescription.renderer.models.Organization;
import bio.ferlab.clin.prescription.renderer.models.Patient;
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
  public CompletableFuture<Organization> getOrganizationById(String authorization, String id) {
    return CompletableFuture.supplyAsync(() -> client.getOrganizationById(authorization, id), es);
  }
  
}
