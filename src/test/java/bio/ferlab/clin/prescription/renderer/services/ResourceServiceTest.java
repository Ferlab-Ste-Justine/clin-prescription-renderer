package bio.ferlab.clin.prescription.renderer.services;

import bio.ferlab.clin.prescription.renderer.exceptions.ResourceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceServiceTest {
  
  private final ResourceService service = new ResourceService();
  
  @Test
  void loadImage() {
    final String base64Image = service.getImageBase64("test.png");
    assertTrue(base64Image.startsWith("data:image/png;base64,"));
  }

  @Test
  void notFound() {
    ResourceException exception = assertThrows(ResourceException.class, () -> {
      service.getImageBase64("foo.jpg");
    });
    assertEquals("Resource not found: foo.jpg", exception.getMessage());
  }

}