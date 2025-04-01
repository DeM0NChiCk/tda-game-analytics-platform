package ru.itis.tdagameanalytics.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.itis.tdagameanalytics.dto.response.ProjectStatistics;
import ru.itis.tdagameanalytics.model.Project;
import ru.itis.tdagameanalytics.model.User;
import ru.itis.tdagameanalytics.service.ProjectService;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<Project> createProject(
            @RequestBody Project project,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String userId = ((User) userDetails).getId();
        project.setUserId(userId);
        return new ResponseEntity<>(
                projectService.createProject(project),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/stats")
    public ResponseEntity<ProjectStatistics> getProjectsStatistics(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String userId = ((User) userDetails).getId();
        return ResponseEntity.ok(projectService.calculateUserStatistics(userId));
    }

    @GetMapping
    public ResponseEntity<List<Project>> getUserProjects(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String userId = ((User) userDetails).getId();
        return ResponseEntity.ok(projectService.getUserProjects(userId));
    }


}
