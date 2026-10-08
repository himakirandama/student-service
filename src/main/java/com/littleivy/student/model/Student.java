package com.littleivy.student.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "student")
public class Student {

    @Id
    @Column(length = 225)
    private String id; // Enroll ID, e.g. LITTLE26-001

    @Column(name = "student_name", nullable = false)
    private String studentName;

    @Column(name = "parent_name", nullable = false)
    private String parentName;

    @Column(nullable = false)
    private String relation;

    @Column(name = "mail_id")
    private String mailId;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(nullable = false, length = 500)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String pincode;

    @Column(nullable = false)
    private String status = "pending"; // pending | accepted | rejected

    @Column(name = "started_at", nullable = false, updatable = false)
    private LocalDateTime startedAt;

    @PrePersist
    protected void onCreate() {
        this.startedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = "pending";
        }
    }

    public Student() {
    }

    public Student(String id, String studentName, String parentName, String relation, String mailId,
                   String phoneNumber, String address, String city, String state, String pincode) {
        this.id = id;
        this.studentName = studentName;
        this.parentName = parentName;
        this.relation = relation;
        this.mailId = mailId;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.status = "pending";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }
}
