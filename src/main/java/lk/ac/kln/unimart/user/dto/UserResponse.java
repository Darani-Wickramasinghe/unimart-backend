package lk.ac.kln.unimart.user.dto;

import lk.ac.kln.unimart.common.Role;

public record UserResponse(Long id, String universityEmail, String fullName, Role role) {}