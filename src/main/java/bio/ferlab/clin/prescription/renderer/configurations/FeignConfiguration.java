package bio.ferlab.clin.prescription.renderer.configurations;

import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class FeignConfiguration {

  private final ObjectFactory<HttpMessageConverters> messageConverters = HttpMessageConverters::new;
  
  @Bean
  public Encoder encoder() {
    return new SpringEncoder(messageConverters);
  }

  @Bean
  public Decoder decoder() {
    return new SpringDecoder(messageConverters);
  }

  @Bean
  public ErrorDecoder errorDecoder() {
    return (method, response) -> new ResponseStatusException(HttpStatus.resolve(response.status()), response.reason());
  }

  @Bean("feignExecutorService")
  public ExecutorService executorService() {
    return Executors.newCachedThreadPool();
  }

}
