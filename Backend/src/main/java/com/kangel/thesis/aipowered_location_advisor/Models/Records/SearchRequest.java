package com.kangel.thesis.aipowered_location_advisor.Models.Records;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SearchRequest(
        @NotNull String userInput,
        @NotNull Location location,
        @NotNull @Positive @Max(20000) int radius) {
}
