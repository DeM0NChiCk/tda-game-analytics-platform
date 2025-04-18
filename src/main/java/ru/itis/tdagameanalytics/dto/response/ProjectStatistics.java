package ru.itis.tdagameanalytics.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.tdagameanalytics.dto.data.MetricData;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectStatistics {
    private SystemMetrics system;
    private CustomMetrics custom;
    private SessionInfo session;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SystemMetrics {
        private Integer fps;
        private Integer memory;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomMetrics {
        private Integer levelProgress;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SessionInfo {
        private String sessionId;
        private DeviceInfo deviceInfo;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class DeviceInfo {
            private String userAgent;
            private String platform;
        }
    }
}
