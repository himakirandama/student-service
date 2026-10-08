package com.littleivy.student.service;

import com.littleivy.student.dto.ApplyRequest;
import com.littleivy.student.event.StudentAppliedEvent;
import com.littleivy.student.event.StudentEventProducer;
import com.littleivy.student.model.Student;
import com.littleivy.student.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentEventProducer eventProducer;
    private final EmailService emailService;

    public StudentService(StudentRepository studentRepository, StudentEventProducer eventProducer,
                           EmailService emailService) {
        this.studentRepository = studentRepository;
        this.eventProducer = eventProducer;
        this.emailService = emailService;
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

        emailService.sendApplicationReceivedEmail(
                saved.getMailId(),
                saved.getParentName(),
                saved.getStudentName(),
                saved.getId()
        );

        return saved;
    }

    private String generateEnrollId() {
        String year = String.valueOf(Year.now().getValue()).substring(2); // e.g. "26"
        long count = studentRepository.countByYear(year) + 1;
        String sequence = String.format("%03d", count);
        return "LITTLE" + year + "-" + sequence; // e.g. LITTLE26-001
    }
}
