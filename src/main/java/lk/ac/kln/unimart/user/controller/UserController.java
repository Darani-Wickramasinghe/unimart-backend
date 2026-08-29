package lk.ac.kln.unimart.user.controller;

import lk.ac.kln.unimart.common.exception.ResourceNotFoundException;
import lk.ac.kln.unimart.user.dto.UserResponse;
import lk.ac.kln.unimart.user.entity.User;
import lk.ac.kln.unimart.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserRepository users;

    public UserController(UserRepository users) {
        this.users = users;
    }

    @GetMapping("/me")
    public UserResponse me(Authentication authentication) {
        User user = users.findByUniversityEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new UserResponse(user.getId(), user.getUniversityEmail(), user.getFullName(), user.getRole());
    }
}