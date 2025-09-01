package com.kangel.thesis.aipowered_location_advisor.Services;

import org.springframework.stereotype.Service;

import com.kangel.thesis.aipowered_location_advisor.Models.Enums.AiProvider;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.AiRequest;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ApiResponse;

@Service
public class AiManager {

    private final OpenAiService openAiService;

    public AiManager(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    public <T> ApiResponse<T> Call(AiProvider provider, AiRequest<T> request) {
        return switch (provider) {
            case OPENAI -> new ApiResponse<T>(true, "Retrieved Ai response", openAiService.Call(request));
            // case CLAUDE etc
            default -> new ApiResponse<T>(false, "Provider not supported", null);
        };
    }

}
