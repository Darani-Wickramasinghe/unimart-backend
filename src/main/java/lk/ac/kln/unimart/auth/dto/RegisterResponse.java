package lk.ac.kln.unimart.auth.dto;

import lk.ac.kln.unimart.common.Role;

public record RegisterResponse(
        Long id,
        String universityEmail,
        String fullName,
        Role role
) {}