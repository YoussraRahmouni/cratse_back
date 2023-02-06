package fr.tse.poc.controller;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import fr.tse.poc.domain.Imputation;
import fr.tse.poc.domain.Project;
import fr.tse.poc.domain.User;
import fr.tse.poc.service.PdfService;
import fr.tse.poc.service.UserService;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.*;
import java.util.List;

@RestController
public class ExportPdfController {

    @Autowired
    private PdfService pdfService;

    @Autowired
    private UserService userService;

    @Autowired
    ServletContext servletContext;

    @GetMapping("/exportPdf/{userId}")
    public ResponseEntity<byte[]> generatePdf(@PathVariable(value = "userId") Long userId,
                                              @RequestParam(value = "month") Integer month,
                                              @RequestParam(value = "year") Integer year,
                                              Principal principal) throws TemplateException, IOException {
        User user = this.userService.checkUserExists(userId);
        this.userService.checkRole(principal, user);
        Map<Project, Map<String, List<Imputation>>> imputations = this.pdfService.getDataExportPdf(user, month, year);

        YearMonth yearMonth = YearMonth.of(year, month);
        int numOfDaysInMonth = yearMonth.lengthOfMonth();

        List<LocalDate> dates = new ArrayList<>();
        List<String> datesString = new ArrayList<>();
        for (int i = 1; i <= numOfDaysInMonth; i++) {
            LocalDate date = yearMonth.atDay(i);
            dates.add(date);
            datesString.add(date.toString());
        }

        Map<String, Object> model = new HashMap<>();
        String userName = user.getFirstName() + " " + user.getLastName();
        model.put("user", userName);
        model.put("month", Month.of(month).name());
        model.put("year", year);
        model.put("dates", dates);
        model.put("datesString", datesString);
        model.put("imputations", imputations);

        String html = this.pdfService.generatePDF(model);
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        ConverterProperties converterProperties = new ConverterProperties();
        HtmlConverter.convertToPdf(html, target, converterProperties);

        // Extract output as bytes
        byte[] bytes = target.toByteArray();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=compte-rendu-" + userName
                        + "-" + month + "-" + year + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);

    }

}
