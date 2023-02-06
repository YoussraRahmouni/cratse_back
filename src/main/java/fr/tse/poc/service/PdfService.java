package fr.tse.poc.service;

import fr.tse.poc.domain.Imputation;
import fr.tse.poc.domain.Project;
import fr.tse.poc.domain.User;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PdfService {
    private static final String TEMPLATE_NAME = "exportPdf.ftl";

    private final Configuration freemarkerConfig;

    @Autowired
    private ImputationService imputationService;

    public PdfService(Configuration freemarkerConfig) {
        this.freemarkerConfig = freemarkerConfig;
    }

    public String generatePDF(Map<String, Object> data) throws IOException, TemplateException {
        freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates");
        Template template = freemarkerConfig.getTemplate(TEMPLATE_NAME);

        StringWriter outputWriter = new StringWriter();
        template.process(data, outputWriter);
        return outputWriter.toString();
    }

    public Map<Project, Map<String, List<Imputation>>> getDataExportPdf(User user, Integer month, Integer year){
        List<Imputation> imputations = this.imputationService.findImputationByUserInMonthAndYear(user, month, year);
        Map<Project, List<Imputation>> imputationsByProject = imputations.stream()
                .collect(Collectors.groupingBy(Imputation::getProject));
        Map<Project, Map<String, List<Imputation>>> imputationsByProjectAndDate = new HashMap<>();
        for (Map.Entry<Project, List<Imputation>> entry : imputationsByProject.entrySet()) {
            Map<String, List<Imputation>> imputationsByDate = entry.getValue().stream()
                    .collect(Collectors.groupingBy(imputation -> imputation.getDateImputation().toString()));
            imputationsByProjectAndDate.put(entry.getKey(), imputationsByDate);
        }
        return imputationsByProjectAndDate;
    }

}