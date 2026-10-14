package com.littleivy.student.service;

import com.littleivy.student.dto.ApplyRequest;
import com.littleivy.student.event.StudentAppliedEvent;
import com.littleivy.student.event.StudentEventProducer;
import com.littleivy.student.model.Student;
import com.littleivy.student.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Year;

@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;
    private final StudentEventProducer eventProducer;
    private final EmailService emailService;
    private final RestTemplate restTemplate;

    @Value("${app.admin-portal.base-url}")
    private String adminPortalBaseUrl;

    public StudentService(StudentRepository studentRepository, StudentEventProducer eventProducer,
                           EmailService emailService, RestTemplate restTemplate) {
        this.studentRepository = studentRepository;
        this.eventProducer = eventProducer;
        this.emailService = emailService;
        this.restTemplate = restTemplate;
    }

    public Student applyForAdmission(ApplyRequest request) {
        String enrollId = generateEnrollId();

        Student student = new Student(
                enrollId,
                request.getStudentName(),
                request.getParentName(),
                request.getRelation(),
                request.getMailId(),
                request.getPhoneNumber(),
                request.getAddress(),
                request.getCity(),
                request.getState(),
                request.getPincode()
        );

        Student saved = studentRepository.save(student);

        StudentAppliedEvent event = new StudentAppliedEvent(
                saved.getId(),
                saved.getStudentName(),
                saved.getParentName(),
                saved.getRelation(),
                saved.getMailId(),
                saved.getPhoneNumber(),
                saved.getAddress(),
                saved.getCity(),
                saved.getState(),
                saved.getPincode(),
                saved.getStatus(),
                saved.getStartedAt()
        );
        eventProducer.publishStudentApplied(event);
        pushSnapshotToAdminPortal(event);

        emailService.sendApplicationReceivedEmail(
                saved.getMailId(),
                saved.getParentName(),
                saved.getStudentName(),
                saved.getId()
        );

        return saved;
    }

    /**
     * Reliable delivery path for the admin portal since Kafka may not be
     * available in all environments. Best-effort: failures are logged but
     * never break the admission flow (Kafka consumer remains a backup path
     * when a broker is configured).
     */
    private void pushSnapshotToAdminPortal(StudentAppliedEvent event) {
        try {
            restTemplate.postForEntity(adminPortalBaseUrl + "/api/internal/student-snapshot", event, Void.class);
        } catch (Exception e) {
            log.warn("Failed to push student snapshot to admin-portal-service for enrollId={}: {}",
                    event.getId(), e.getMessage());
        }
    }

    private String generateEnrollId() {
        String year = String.valueOf(Year.now().getValue()).substring(2); // e.g. "26"
        long count = studentRepository.countByYear(year) + 1;
        String sequence = String.format("%03d", count);
        return "LITTLE" + year + "-" + sequence; // e.g. LITTLE26-001
    }
}
