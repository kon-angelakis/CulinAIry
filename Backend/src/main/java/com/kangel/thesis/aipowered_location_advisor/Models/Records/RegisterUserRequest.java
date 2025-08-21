package com.kangel.thesis.aipowered_location_advisor.Models.Records;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record RegisterUserRequest(
        @NotNull @Valid UserDTO user,
        String otp) {
}
