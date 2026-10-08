package com.littleivy.student.event;

import java.time.LocalDateTime;

public class StudentAppliedEvent {

    private String id;
    private String studentName;
    private String parentName;
    private String relation;
    private String mailId;
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String status;
    private LocalDateTime startedAt;

    public StudentAppliedEvent() {
    }

    public StudentAppliedEvent(String id, String studentName, String parentName, String relation, String mailId,
                                String phoneNumber, String address, String city, String state, String pincode,
                                String status, LocalDateTime startedAt) {
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
        this.status = status;
        this.startedAt = startedAt;
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

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }
}
