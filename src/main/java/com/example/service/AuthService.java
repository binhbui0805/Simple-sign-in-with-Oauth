package com.example.service;

import com.example.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthService(UserRepository users, PasswordEncoder encoder, JwtService jwt) {
        this.users = users;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    public String login(String username, char[] rawPassword) {
        var user = users.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!encoder.matches(new String(rawPassword), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return jwt.generateToken(user.getUsername(), user.getRole());
    }
}
