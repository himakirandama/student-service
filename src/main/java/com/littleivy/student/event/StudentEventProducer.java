package com.littleivy.student.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class StudentEventProducer {

    private static final Logger log = LoggerFactory.getLogger(StudentEventProducer.class);
    private static final String TOPIC = "student-applied";

    private final KafkaTemplate<String, StudentAppliedEvent> kafkaTemplate;

    public StudentEventProducer(KafkaTemplate<String, StudentAppliedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishStudentApplied(StudentAppliedEvent event) {
        try {
            kafkaTemplate.send(TOPIC, event.getId(), event)
                    .exceptionally(ex -> {
                        log.warn("Failed to publish student-applied event for {}: {}", event.getId(), ex.getMessage());
                        return null;
                    });
        } catch (Exception e) {
            // Catch-all so a Kafka outage never breaks the admission flow
            log.warn("Unexpected error publishing student-applied event for {}: {}", event.getId(), e.getMessage());
        }
    }
}

