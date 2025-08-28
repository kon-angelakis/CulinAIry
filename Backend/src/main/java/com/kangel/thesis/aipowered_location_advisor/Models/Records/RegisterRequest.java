package com.kangel.thesis.aipowered_location_advisor.Models.Records;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        String firstName,
        String lastName,
        @NotBlank String email,
        @NotBlank String username,
        @NotBlank String password,
        String pfp) {

}
