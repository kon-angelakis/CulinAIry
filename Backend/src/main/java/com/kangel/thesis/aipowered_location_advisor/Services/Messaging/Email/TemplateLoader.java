package com.kangel.thesis.aipowered_location_advisor.Services.Messaging.Email;

import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class TemplateLoader {

    private final ResourceLoader resourceLoader;

    public TemplateLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String load(String path) {
        try (var in = resourceLoader.getResource("classpath:email-templates/" + path).getInputStream()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load template " + path, e);
        }
    }
}