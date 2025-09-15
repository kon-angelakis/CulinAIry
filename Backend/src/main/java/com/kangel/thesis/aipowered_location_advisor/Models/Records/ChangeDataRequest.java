package com.kangel.thesis.aipowered_location_advisor.Models.Records;

import jakarta.validation.constraints.NotBlank;

public record ChangeDataRequest(
        @NotBlank String username,
        String firstName,
        String lastName) {

}
