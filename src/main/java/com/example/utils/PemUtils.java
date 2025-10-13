package com.example.utils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

public final class PemUtils {
    private PemUtils() {}

    public static RSAPrivateKey loadPrivateKey(String classpathPem) {
        try (InputStream is = new ClassPathResource(classpathPem).getInputStream()) {
            String pem = StreamUtils.copyToString(is, StandardCharsets.UTF_8)
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");
            byte[] der = Base64.getDecoder().decode(pem);
            var spec = new PKCS8EncodedKeySpec(der);
            var kf = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) kf.generatePrivate(spec);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load private key from " + classpathPem, e);
        }
    }
}