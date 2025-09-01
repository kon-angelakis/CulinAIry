package com.kangel.thesis.aipowered_location_advisor.Models.Records;

public record AiRequest<T>(
        String userPrompt,
        String instructionsPromptLocation,
        Class<T> responseClass) {

}
