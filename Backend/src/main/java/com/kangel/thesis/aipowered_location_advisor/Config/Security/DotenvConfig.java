package com.kangel.thesis.aipowered_location_advisor.Config.Security;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotenvConfig {

    @Bean
    public Dotenv dotenv() { // Load the environmental variables from the .env file
        return Dotenv.load();
    }
}