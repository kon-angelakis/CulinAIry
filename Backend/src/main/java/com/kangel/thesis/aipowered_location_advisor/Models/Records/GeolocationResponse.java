package com.kangel.thesis.aipowered_location_advisor.Models.Records;

public record GeolocationResponse(
                String status,
                String city,
                double lat, double lon,
                String query) {

}
