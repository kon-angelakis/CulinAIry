package com.kangel.thesis.aipowered_location_advisor.Models.Records;

public record PlaceDTO(
        String id,
        String thumbnail,
        String name,
        String primaryType,
        double rating,
        int totalRatings

) {

}
