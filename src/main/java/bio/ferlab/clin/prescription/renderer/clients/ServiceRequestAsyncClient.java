package bio.ferlab.clin.prescription.renderer.clients;

import bio.ferlab.clin.prescription.renderer.models.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
public class ServiceRequestAsyncClient {
  
  @Autowired
  @Qualifier("feignExecutorService")
  private ExecutorService es;
  
  @Autowired
  private ServiceRequestClient client;

  public CompletableFuture<Resource> getPatientById(String authorization, String id) {
    return CompletableFuture.supplyAsync(() -> client.getPatientById(authorization, id), es);
  }
}
