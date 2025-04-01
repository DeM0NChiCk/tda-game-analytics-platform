package ru.itis.tdagameanalytics.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.itis.tdagameanalytics.dto.data.MetricData;
import ru.itis.tdagameanalytics.model.User;
import ru.itis.tdagameanalytics.service.ProjectService;

import java.security.Principal;

@RequiredArgsConstructor
@Component
public class DashboardController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ProjectService projectService;

    @MessageMapping("/metrics/{projectId}")
    public void handleMetrics(
            @DestinationVariable String projectId,
            @Payload MetricData metric,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String userId = ((User) userDetails).getId();
        if (projectService.hasAccess(userId, projectId)) {
            messagingTemplate.convertAndSend("/topic/metrics/" + projectId, metric);
        }
    }
}
