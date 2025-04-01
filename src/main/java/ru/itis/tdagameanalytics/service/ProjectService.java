package ru.itis.tdagameanalytics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.tdagameanalytics.dto.data.MetricData;
import ru.itis.tdagameanalytics.dto.response.ProjectStatistics;
import ru.itis.tdagameanalytics.model.Project;
import ru.itis.tdagameanalytics.repository.ProjectRepository;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectStatistics calculateUserStatistics(String userId) {
        List<Project> projects = projectRepository.findAllByUserId(userId);
        return calculateStatistics(projects);
    }

    private ProjectStatistics calculateStatistics(List<Project> projects) {
        if (projects == null || projects.isEmpty()) {
            return ProjectStatistics.builder().build();
        }

        int sumFps = 0;
        int sumMemory = 0;
        int sumLevelProgress = 0;
        int systemCount = 0;
        int customCount = 0;

        for (Project project : projects) {
            if (project.getSystem() != null) {
                sumFps += project.getSystem().getFps();
                sumMemory += project.getSystem().getMemory();
                systemCount++;
            }
            if (project.getCustom() != null) {
                sumLevelProgress += project.getCustom().getLevelProgress();
                customCount++;
            }
        }

        int avgFps = systemCount > 0 ? sumFps / systemCount : 0;
        int avgMemory = systemCount > 0 ? sumMemory / systemCount : 0;
        int avgLevelProgress = customCount > 0 ? sumLevelProgress / customCount : 0;

        Project firstProject = projects.get(0);
        Project.SessionInfo projectSessionInfo = firstProject.getSession();
        String sessionId = null;
        ProjectStatistics.SessionInfo.DeviceInfo deviceInfo = null;

        if (projectSessionInfo != null) {
            sessionId = projectSessionInfo.getSessionId();
            Project.SessionInfo.DeviceInfo sourceDevice = projectSessionInfo.getDeviceInfo();
            if (sourceDevice != null) {
                deviceInfo = ProjectStatistics.SessionInfo.DeviceInfo.builder()
                        .userAgent(sourceDevice.getUserAgent())
                        .platform(sourceDevice.getPlatform())
                        .build();
            }
        }

        ProjectStatistics.SystemMetrics systemMetrics = ProjectStatistics.SystemMetrics.builder()
                .fps(avgFps)
                .memory(avgMemory)
                .build();

        ProjectStatistics.CustomMetrics customMetrics = ProjectStatistics.CustomMetrics.builder()
                .levelProgress(avgLevelProgress)
                .build();

        ProjectStatistics.SessionInfo sessionInfo = ProjectStatistics.SessionInfo.builder()
                .sessionId(sessionId)
                .deviceInfo(deviceInfo) // Исправлено: передаем созданный deviceInfo
                .build();

        return ProjectStatistics.builder()
                .system(systemMetrics)
                .custom(customMetrics)
                .session(sessionInfo)
                .build();
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public List<Project> getUserProjects(String userId) {
        return projectRepository.findAllByUserId(userId);
    }

    public boolean hasAccess(String userId, String projectId) {
        return projectRepository.existsByIdAndUserId(projectId, userId);
    }
}
