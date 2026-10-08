package com.littleivy.student.service;

import com.littleivy.student.dto.EmailTemplateResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private static final String DEFAULT_SUBJECT = "Little Ivy School - Application Received ({{enrollId}})";
    private static final String DEFAULT_BODY =
            "<p>Dear {{parentName}},</p>"
                    + "<p>Thank you for applying to Little Ivy School on behalf of <strong>{{studentName}}</strong>.</p>"
                    + "<p>Your enroll ID is: <strong>{{enrollId}}</strong></p>"
                    + "<p>Please keep this ID for future reference. Our admissions team will review the application "
                    + "and get back to you shortly with a decision.</p>"
                    + "<p>Warm regards,<br/>Little Ivy School Admissions Team</p>";

    private final JavaMailSender mailSender;
    private final RestTemplate restTemplate;

    @Value("${app.mail.from}")
    private String fromAddress;

    @Value("${app.mail.from-name}")
    private String fromName;

    @Value("${app.admin-portal.base-url}")
    private String adminPortalBaseUrl;

    public EmailService(JavaMailSender mailSender, RestTemplate restTemplate) {
        this.mailSender = mailSender;
        this.restTemplate = restTemplate;
    }

    public void sendApplicationReceivedEmail(String toEmail, String parentName, String studentName, String enrollId) {
        Map<String, String> placeholders = Map.of(
                "parentName", parentName,
                "studentName", studentName,
                "enrollId", enrollId
        );

        String subjectTemplate = DEFAULT_SUBJECT;
        String bodyTemplate = DEFAULT_BODY;

        try {
            EmailTemplateResponse template = restTemplate.getForObject(
                    adminPortalBaseUrl + "/api/email-templates/ENROLLMENT", EmailTemplateResponse.class);
            if (template != null && template.getSubject() != null && template.getBody() != null) {
                subjectTemplate = template.getSubject();
                bodyTemplate = template.getBody();
            }
        } catch (Exception e) {
            log.warn("Could not fetch ENROLLMENT email template from admin-portal-service, using default. Reason: {}",
                    e.getMessage());
        }

        String subject = applyPlaceholders(subjectTemplate, placeholders);
        String body = applyPlaceholders(bodyTemplate, placeholders);

        send(toEmail, subject, body);
    }

    private String applyPlaceholders(String text, Map<String, String> placeholders) {
        String result = text;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            result = result.replace("{{" + entry.getKey() + "}}", entry.getValue() == null ? "" : entry.getValue());
        }
        return result;
    }

    private void send(String toEmail, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setFrom(fromAddress, fromName);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
            log.info("Sent email to {} with subject '{}'", toEmail, subject);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Failed to send email to {}: {}", toEmail, e.getMessage());
        } catch (Exception e) {
            // Catch-all so a mail server outage never breaks the admission flow
            log.error("Unexpected error sending email to {}: {}", toEmail, e.getMessage());
        }
    }
}
