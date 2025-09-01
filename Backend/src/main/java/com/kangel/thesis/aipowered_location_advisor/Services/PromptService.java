package com.kangel.thesis.aipowered_location_advisor.Services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class PromptService {

    private final ResourceLoader resourceLoader;
    private final Map<String, String> cache = new ConcurrentHashMap<>(); // Thread safe cache

    public PromptService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String GetPrompt(String name) {
        return cache.computeIfAbsent(name, this::LoadFile);
    }

    public String LoadFile(String name) {
        try {
            var res = resourceLoader.getResource(String.format("classpath:ai-system-prompts/%s.txt", name));
            return new String(res.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Prompt loading failed", e);
        }
    }

}
