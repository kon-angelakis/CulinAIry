package com.kangel.thesis.aipowered_location_advisor.Services;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.kangel.thesis.aipowered_location_advisor.Config.Exceptions.MissingRequestFieldException;

@Service
public class RequestValidator { //Validates frontend requests that are missing key parameters
    public void validateRequiredKeys(Map<String, Object> body, String... requiredKeys) {
        ArrayList<String> missingKeys = new ArrayList<>();
        for (String key : requiredKeys) {
            if (!body.containsKey(key) || body.get(key) == null || body.get(key).toString().isBlank()) {
                missingKeys.add(key);
            }
        }
        if(missingKeys.size() != 0)
            throw new MissingRequestFieldException("Missing REQUIRED fields: " + missingKeys.toString().replaceAll("[\\[\\]]", ""));

    }
}
