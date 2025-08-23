package com.kangel.thesis.aipowered_location_advisor.Models.Records;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record NearbySearchRequest(
        @NotNull String restaurant_types,
        @NotNull int num_of_places,
        @NotNull Location location,
        @Positive @Max(20000) int radius) {

}
