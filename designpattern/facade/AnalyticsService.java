package facade;

import java.time.LocalDateTime;

// Subsystem 6 — tracks signup event for analytics
public class AnalyticsService {

    public void trackSignup(String userId, String email) {
        System.out.println("[AnalyticsService]  Tracking signup event...");
        System.out.println("[AnalyticsService]  Event: USER_REGISTERED | userId=" + userId
                + " | timestamp=" + LocalDateTime.now());
    }
}