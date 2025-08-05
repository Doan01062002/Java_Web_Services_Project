package com.example.project_java_web_service_nguyenvandoan.scheduler;

import com.example.project_java_web_service_nguyenvandoan.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SessionCleanupScheduler {

    private static final Logger logger = LoggerFactory.getLogger(SessionCleanupScheduler.class);

    @Autowired
    private UserService userService;

    @Scheduled(cron = "0 0 * * * *") // Run hourly
    public void cleanupExpiredSessions() {
        try {
            int deletedCount = userService.cleanupExpiredSessions();
            if (deletedCount > 0) {
                logger.info("Deleted {} expired sessions.", deletedCount);
            } else {
                logger.debug("No expired sessions found to delete.");
            }
        } catch (Exception e) {
            logger.error("Error during session cleanup: {}", e.getMessage(), e);
        }
    }
}