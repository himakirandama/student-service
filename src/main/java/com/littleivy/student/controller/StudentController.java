package com.littleivy.student.controller;

import com.littleivy.student.dto.ApplyRequest;
import com.littleivy.student.model.Student;
import com.littleivy.student.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/apply")
    public ResponseEntity<Student> apply(@Valid @RequestBody ApplyRequest request) {
        Student student = studentService.applyForAdmission(request);
        return ResponseEntity.ok(student);
    }
}
