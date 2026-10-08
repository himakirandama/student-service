package com.littleivy.student.repository;

import com.littleivy.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentRepository extends JpaRepository<Student, String> {

    @Query(value = "SELECT COUNT(*) FROM student WHERE id LIKE CONCAT('LITTLE', :year, '-%')", nativeQuery = true)
    long countByYear(String year);
}
