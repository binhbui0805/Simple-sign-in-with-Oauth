package com.example.controller;

import java.util.Arrays;
import java.util.Map;

import com.example.model.LoginRequest;
import com.example.model.RegisterRequest;
import com.example.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {

        String token = authService.login(req.getUsername(), req.getPassword());
        return ResponseEntity.ok(Map.of("access_token", token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        try {
            authService.register(req.getUsername(), req.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "User created"));
        } finally {
            if (req.getPassword() != null) Arrays.fill(req.getPassword(), '\0');
        }
    }


}
