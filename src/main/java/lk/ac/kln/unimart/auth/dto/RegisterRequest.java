package lk.ac.kln.unimart.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lk.ac.kln.unimart.common.Role;

public record RegisterRequest(
        @NotBlank @Email String universityEmail,
        @NotBlank @Size(min = 8, message = "Password must be at least 8 characters") String password,
        @NotBlank String fullName,
        Role role
) {}