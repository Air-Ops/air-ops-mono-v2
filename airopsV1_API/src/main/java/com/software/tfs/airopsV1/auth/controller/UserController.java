package com.software.tfs.airopsV1.auth.controller;

import com.software.tfs.airopsV1.auth.dto.request.LoginRequest;
import com.software.tfs.airopsV1.auth.dto.request.RegistrationRequest;
import com.software.tfs.airopsV1.auth.dto.response.LoginResponse;
import com.software.tfs.airopsV1.auth.dto.response.RegistrationResponse;
import com.software.tfs.airopsV1.auth.repo.UserRepository;
import com.software.tfs.airopsV1.auth.service.JwtService;
import com.software.tfs.airopsV1.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.autoconfigure.JacksonProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ao/api/auth")
public class UserController {

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserController(JwtService jwtService, UserRepository userRepository, UserService userService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("xyz")
    public ResponseEntity<?> xyz() {
        try {
            return ResponseEntity.ok("User Controller Online");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        RegistrationResponse result = this.userService.register(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        LoginResponse response = this.userService.login(request);
        return ResponseEntity.ok(response);
    }
}
