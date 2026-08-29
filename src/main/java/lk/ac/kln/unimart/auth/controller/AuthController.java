package lk.ac.kln.unimart.auth.controller;

import jakarta.validation.Valid;
import lk.ac.kln.unimart.auth.dto.AuthResponse;
import lk.ac.kln.unimart.auth.dto.LoginRequest;
import lk.ac.kln.unimart.auth.dto.RegisterRequest;
import lk.ac.kln.unimart.auth.dto.RegisterResponse;
import lk.ac.kln.unimart.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}