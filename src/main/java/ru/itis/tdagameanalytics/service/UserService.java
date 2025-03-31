package ru.itis.tdagameanalytics.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.tdagameanalytics.config.security.service.JwtService;
import ru.itis.tdagameanalytics.dto.request.UserAuthenticationRequest;
import ru.itis.tdagameanalytics.dto.request.UserRegistrationRequest;
import ru.itis.tdagameanalytics.dto.request.UserUpdateRequest;
import ru.itis.tdagameanalytics.dto.response.AuthenticationResponse;
import ru.itis.tdagameanalytics.enums.UserRole;
import ru.itis.tdagameanalytics.exceptions.UserAlreadyExistsException;
import ru.itis.tdagameanalytics.exceptions.UserNotFoundException;
import ru.itis.tdagameanalytics.model.User;
import ru.itis.tdagameanalytics.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Пользователь с email " + request.getEmail() + " уже существует");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER)
                .build();

        User savedUser = userRepository.save(user);

        return generateAuthResponse(savedUser);
    }

    public AuthenticationResponse authenticate(UserAuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        return generateAuthResponse(user);
    }

    public User getCurrentUser(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    public User updateUser(String userId, UserUpdateRequest updateRequest, UserDetails currentUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        if (!user.getId().equals(currentUser.getUsername())) {
            throw new SecurityException("Нет прав для изменения этого аккаунта");
        }

        if (updateRequest.getUsername() != null) {
            user.setUsername(updateRequest.getUsername());
        }

        if (updateRequest.getEmail() != null && !updateRequest.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(updateRequest.getEmail())) {
                throw new UserAlreadyExistsException("Email уже используется");
            }
            user.setEmail(updateRequest.getEmail());
        }

        return userRepository.save(user);
    }

    public void deleteUser(String userId, UserDetails currentUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        if (!user.getId().equals(currentUser.getUsername())) {
            throw new SecurityException("Нет прав для удаления этого аккаунта");
        }

        userRepository.delete(user);
    }

    private AuthenticationResponse generateAuthResponse(User user) {
        String jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}