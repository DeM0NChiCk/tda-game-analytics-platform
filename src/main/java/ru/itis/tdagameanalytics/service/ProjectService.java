package ru.itis.tdagameanalytics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
        DoubleSummaryStatistics fpsStats = projects.stream()
                .mapToDouble(p -> p.getSystem().getFps())
                .summaryStatistics();

        Map<String, Long> browserStats = projects.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getSession().getDeviceInfo().getUserAgent(),
                        Collectors.counting()));

        return ProjectStatistics.builder()
                .averageFps(fpsStats.getAverage())
                .browserDistribution(browserStats)
                .totalProjects(projects.size())
                .minFps(fpsStats.getMin())
                .maxFps(fpsStats.getMax())
                .build();
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public List<Project> getUserProjects(String userId) {
        return projectRepository.findAllByUserId(userId);
    }
}
