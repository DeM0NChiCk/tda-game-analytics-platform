package ru.itis.tdagameanalytics.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ProjectStatistics {
    private double averageFps;
    private Map<String, Long> browserDistribution;
    private int totalProjects;
    private double minFps;
    private double maxFps;
}
