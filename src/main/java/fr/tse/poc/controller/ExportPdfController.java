package fr.tse.poc.controller;

import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ExportPdfController {

//    @Autowired
//    private Configuration freemarkerConfig;
//
//    @GetMapping("/pdf")
//    public ModelAndView generatePDF() {
//        Map<String, Object> model = new HashMap<>();
//        model.put("name", "John Doe");
//
//        return new ModelAndView(new PDFView(), model);
//    }
}
