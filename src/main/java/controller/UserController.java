package com.example.usermanagement.controller;

import com.example.usermanagement.dto.LoginRequest;
import com.example.usermanagement.entity.User;
import com.example.usermanagement.security.JwtUtil;
import com.example.usermanagement.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        User user = userService.login(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
        return jwtUtil.generateToken(user.getEmail());
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    // ✅ PROTECTED ENDPOINT
    @GetMapping("/profile")
    public String profile(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtil.extractEmail(token);
        return "Profile accessed by: " + email;
    }
}
