package com.kangel.thesis.aipowered_location_advisor.Models.Records;

import jakarta.validation.constraints.NotBlank;

//Pass may be blank for field authentication
public record AuthUserRequest(
        @NotBlank String user,
        String pass) {
}
