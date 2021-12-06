package bio.ferlab.clin.prescription.renderer.services;

import bio.ferlab.clin.prescription.renderer.exceptions.ResourceException;
import org.apache.commons.io.IOUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Service
public class ResourceService {
  
  private static final String RESOURCES_CACHE = "RESOURCES_CACHE";
  
  @Cacheable(value = RESOURCES_CACHE, key = "#resource", sync = true)
  public String getImageBase64(String resource) {
    return loadResource(resource, "image");
  }
  
  private String loadResource(String resource, String mediaType){
    String base64;
    try {
      final String format = resource.split("\\.")[1]; // png,jpg ...
      base64 = String.format("data:%s/%s;base64,%s", mediaType, format, convertToBase64(resource));
    } catch (Exception e) {
      throw new ResourceException(e);
    }
    return base64;
  }

  private String convertToBase64(String resource) throws IOException {
    byte[] imageAsBytes;
    try(InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("static/" + resource)) {
      imageAsBytes = IOUtils.toByteArray(inputStream);
    }
    return Base64.getEncoder().encodeToString(imageAsBytes);
  }
}
