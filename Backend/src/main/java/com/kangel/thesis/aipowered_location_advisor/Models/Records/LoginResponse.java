package com.kangel.thesis.aipowered_location_advisor.Models.Records;

public record LoginResponse(
        UserDTO userDetails,
        String jwtToken) {

}
