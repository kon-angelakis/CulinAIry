package com.kangel.thesis.aipowered_location_advisor.Models.Records;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data) {

}
