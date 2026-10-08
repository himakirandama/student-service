package com.littleivy.student.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class StudentEventProducer {

    private static final String TOPIC = "student-applied";

    private final KafkaTemplate<String, StudentAppliedEvent> kafkaTemplate;

    public StudentEventProducer(KafkaTemplate<String, StudentAppliedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishStudentApplied(StudentAppliedEvent event) {
        kafkaTemplate.send(TOPIC, event.getId(), event);
    }
}
