package bio.ferlab.clin.prescription.renderer.services;

import com.lowagie.text.DocumentException;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PdfService {

  public byte[] generateFromHtml(String html) throws IOException, DocumentException {
    byte[] data;
    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      ITextRenderer renderer = new ITextRenderer();
      renderer.setDocumentFromString(html);
      renderer.layout();
      renderer.createPDF(out);
      data = out.toByteArray();
    }
    return data;
  }
  
}
