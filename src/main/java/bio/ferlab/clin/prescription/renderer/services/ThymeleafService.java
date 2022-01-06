package bio.ferlab.clin.prescription.renderer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.messageresolver.IMessageResolver;
import org.thymeleaf.messageresolver.StandardMessageResolver;
import org.thymeleaf.spring5.messageresolver.SpringMessageResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Map;

@Service
public class ThymeleafService {

  private IMessageResolver messageResolver;
  
  @Autowired
  public ThymeleafService(IMessageResolver messageResolver) {
    this.messageResolver = messageResolver;
  }
  
  public String parseTemplate(String templateName, Map<String, Object> params) {
    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setSuffix(".html");
    templateResolver.setPrefix("/templates/");
    templateResolver.setTemplateMode(TemplateMode.HTML);

    TemplateEngine templateEngine = new TemplateEngine();
    templateEngine.setTemplateResolver(templateResolver);
    templateEngine.setMessageResolver(messageResolver);

    Context context = new Context();
    context.setVariables(params);

    return templateEngine.process(templateName, context);
  }
}
