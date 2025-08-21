package com.kangel.thesis.aipowered_location_advisor.Models.Records;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

//Used to parse basic userinfo info between the frontend and backend
public record UserDTO(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank @Email String email,
        @NotBlank String username,
        String password,
        String pfp) {
}
