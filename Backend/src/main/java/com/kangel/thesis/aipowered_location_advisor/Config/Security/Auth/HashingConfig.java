package com.kangel.thesis.aipowered_location_advisor.Config.Security.Auth;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class HashingConfig {

    private final int encoderStrength = 8;
    private final SecureRandom secureRandom = new SecureRandom();

    @Bean
    public BCryptPasswordEncoder PasswordEncoder() {
        return new BCryptPasswordEncoder(encoderStrength);
    }

    public String GenerateRandomUsername(int length) {

        byte[] hash = new byte[length];
        secureRandom.nextBytes(hash);
        String base64Hash = Base64.getEncoder().encodeToString(hash);

        return base64Hash;
    }
}
