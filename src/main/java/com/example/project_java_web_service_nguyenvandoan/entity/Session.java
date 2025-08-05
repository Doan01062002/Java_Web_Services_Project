package com.example.project_java_web_service_nguyenvandoan.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Session {
    @Id
    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "login_time", nullable = false)
    private LocalDateTime loginTime = LocalDateTime.now();

    @Column(name = "last_activity_time", nullable = false)
    private LocalDateTime lastActivityTime = LocalDateTime.now();

    @Column(name = "expiry_time")
    private LocalDateTime expiryTime;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 255)
    private String userAgent;
}