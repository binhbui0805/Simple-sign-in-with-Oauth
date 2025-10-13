package com.example.service;

import com.example.utils.PemUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final RSAPrivateKey privateKey;

    public JwtService() {
        this.privateKey = PemUtils.loadPrivateKey("keys/private.pem");
    }


    public String generateToken(String subject, String role) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setIssuer("https://localhost:8080")           // set your issuer
                .setSubject(subject)
                .addClaims(Map.of("authorities", new String[]{role}))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(3600))) // 1h
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }
}
