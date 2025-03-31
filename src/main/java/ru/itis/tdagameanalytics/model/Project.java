package ru.itis.tdagameanalytics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "projects")
public class Project {
    @Id
    private String id;
    private String userId;
    private SystemMetrics system;
    private CustomMetrics custom;
    private SessionInfo session;
    private Metadata metadata;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SystemMetrics {
        private int fps;
        private int memory;
        private int loadTime;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomMetrics {
        private int levelProgress;
        private List<String> achievements;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SessionInfo {
        private String sessionId;
        private String userId;
        private DeviceInfo deviceInfo;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class DeviceInfo {
            private String userAgent;
            private String platform;
            private String language;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Metadata {
        private long timestamp;
        private int priority;
        private int retries;
    }
}