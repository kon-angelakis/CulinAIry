package com.kangel.thesis.aipowered_location_advisor.Models.Records;

import jakarta.validation.constraints.NotNull;

public record SearchRequest(
        @NotNull String user_input,
        @NotNull Location location,
        @NotNull int radius) {
}
