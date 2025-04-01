package ru.itis.tdagameanalytics.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.tdagameanalytics.dto.request.UserAuthenticationRequest;
import ru.itis.tdagameanalytics.dto.request.UserRegistrationRequest;
import ru.itis.tdagameanalytics.dto.response.AuthenticationResponse;
import ru.itis.tdagameanalytics.service.UserService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody UserRegistrationRequest request
    ) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody UserAuthenticationRequest request
    ) {
        return ResponseEntity.ok(userService.authenticate(request));
    }
}
