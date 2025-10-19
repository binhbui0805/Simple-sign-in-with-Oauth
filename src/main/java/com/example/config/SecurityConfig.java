package com.example.config;

import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.Base64;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.StreamUtils;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filter(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/actuator/health",
                                "/auth/**")
                        .permitAll()
                        .anyRequest().authenticated()
                )
               .oauth2ResourceServer(oauth -> oauth
                       .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .build();
    }

    private org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter jwtAuthenticationConverter() {
        var converter = new org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            var out = new ArrayList<String>();
            Object auths = jwt.getClaims().get("authorities");
            if (auths instanceof Collection<?> c) c.forEach(a -> out.add(a.toString()));
            Object scope = jwt.getClaims().get("scope");
            if (scope instanceof String s) out.addAll(Arrays.stream(s.split(" ")).map(sc -> "SCOPE_" + sc).toList());
            return out.stream().map(org.springframework.security.core.authority.SimpleGrantedAuthority::new).collect(Collectors.toList());
        });
        return converter;
    }

    @Bean
    JwtDecoder jwtDecoder() {

        RSAPublicKey publicKey = loadPublicKey("keys/public.pem");
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    private RSAPublicKey loadPublicKey(String classpathPem) {
        try (InputStream is = new ClassPathResource(classpathPem).getInputStream()) {
            String pem = StreamUtils.copyToString(is, StandardCharsets.UTF_8)
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] der = Base64.getDecoder().decode(pem);
            var spec = new X509EncodedKeySpec(der);
            var kf = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) kf.generatePublic(spec);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load public key", e);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
