package com.kangel.thesis.aipowered_location_advisor.Models.Records;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String user,
        @NotBlank String pass) {

}
