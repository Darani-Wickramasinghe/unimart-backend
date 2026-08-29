package lk.ac.kln.unimart.auth.dto;

public record AuthResponse(
        String accessToken,
        String tokenType,
        long expiresIn
) {}