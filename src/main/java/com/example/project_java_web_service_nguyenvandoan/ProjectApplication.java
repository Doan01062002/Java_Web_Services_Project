package com.example.project_java_web_service_nguyenvandoan;

import com.example.project_java_web_service_nguyenvandoan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class ProjectApplication {

    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

    @Scheduled(fixedRate = 3600000) // Run every hour
    public void cleanupExpiredSessions() {
        userService.cleanupExpiredSessions();
    }
}