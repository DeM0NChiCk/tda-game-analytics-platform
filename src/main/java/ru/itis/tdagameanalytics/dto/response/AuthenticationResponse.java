package ru.itis.tdagameanalytics.dto.response;

import lombok.Builder;
import lombok.Data;
import ru.itis.tdagameanalytics.enums.UserRole;

@Data
@Builder
public class AuthenticationResponse {
    private String token;
    private String userId;
    private String email;
    private String username;
    private UserRole role;
}
