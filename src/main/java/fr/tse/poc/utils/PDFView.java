package fr.tse.poc.utils;

import freemarker.template.Template;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

//public class PDFView extends AbstractView {
//    @Override
//    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        response.setContentType("application/pdf");
//
//        Template template = freemarkerConfig.getTemplate("template.ftl");
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//        Writer writer = new OutputStreamWriter(baos);
//        template.process(model, writer);
//
//        ITextRenderer renderer = new ITextRenderer();
//        renderer.setDocumentFromString(baos.toString());
//        renderer.layout();
//
//        renderer.createPDF(response.getOutputStream());
//    }
//}
