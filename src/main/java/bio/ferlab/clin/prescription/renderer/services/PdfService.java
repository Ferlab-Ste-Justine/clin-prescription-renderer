package bio.ferlab.clin.prescription.renderer.services;

import bio.ferlab.clin.prescription.renderer.exceptions.PdfException;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

  public byte[] generateFromHtml(String html) {
    byte[] data;
    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      ITextRenderer renderer = new ITextRenderer();
      renderer.setDocumentFromString(html);
      renderer.layout();
      renderer.createPDF(out);
      data = out.toByteArray();
    } catch (Exception e) {
      throw new PdfException(e);
    }
    return data;
  }
  
}
