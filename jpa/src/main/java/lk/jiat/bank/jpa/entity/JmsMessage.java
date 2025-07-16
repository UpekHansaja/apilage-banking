package lk.jiat.bank.jpa.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "jms_message")
public class JmsMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private Boolean processed = false;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
