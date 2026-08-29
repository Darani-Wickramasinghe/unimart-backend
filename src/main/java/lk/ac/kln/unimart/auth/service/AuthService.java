package lk.ac.kln.unimart.auth.service;

import lk.ac.kln.unimart.auth.dto.AuthResponse;
import lk.ac.kln.unimart.auth.dto.LoginRequest;
import lk.ac.kln.unimart.auth.dto.RegisterRequest;
import lk.ac.kln.unimart.auth.dto.RegisterResponse;
import lk.ac.kln.unimart.common.Role;
import lk.ac.kln.unimart.common.exception.BadRequestException;
import lk.ac.kln.unimart.common.exception.ConflictException;
import lk.ac.kln.unimart.user.entity.User;
import lk.ac.kln.unimart.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class AuthService {

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final long accessMinutes;

    public AuthService(UserRepository users,
                       PasswordEncoder passwordEncoder,
                       JwtEncoder jwtEncoder,
                       @Value("${app.security.access-minutes}") long accessMinutes) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.accessMinutes = accessMinutes;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (users.existsByUniversityEmail(request.universityEmail())) {
            throw new ConflictException("An account with this email already exists");
        }
        User user = new User();
        user.setUniversityEmail(request.universityEmail());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setFullName(request.fullName());
        user.setRole(request.role() != null ? request.role() : Role.BUYER);
        user.setEmailVerified(false);

        User saved = users.save(user);
        return new RegisterResponse(saved.getId(), saved.getUniversityEmail(), saved.getFullName(), saved.getRole());
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = users.findByUniversityEmail(request.universityEmail())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadRequestException("Invalid email or password");
        }

        Instant now = Instant.now();
        Instant expiry = now.plus(accessMinutes, ChronoUnit.MINUTES);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("unimart")
                .issuedAt(now)
                .expiresAt(expiry)
                .subject(user.getUniversityEmail())
                .claim("role", user.getRole().name())
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();

        return new AuthResponse(token, "Bearer", accessMinutes * 60);
    }
}