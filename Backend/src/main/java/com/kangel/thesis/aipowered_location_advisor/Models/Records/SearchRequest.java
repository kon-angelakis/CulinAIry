package com.kangel.thesis.aipowered_location_advisor.Models.Records;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SearchRequest(
                @NotBlank String userInput,
                @NotNull Location location,
                @NotNull @Positive @Min(500) @Max(20000) int radius,
                PaginationRequest pagingRequest,
                String sortField,
                int sortDirection) {
}
