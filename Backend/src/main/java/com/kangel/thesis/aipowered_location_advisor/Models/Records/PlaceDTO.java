package com.kangel.thesis.aipowered_location_advisor.Models.Records;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

public record PlaceDTO(
        String id,
        String thumbnail,
        String name,
        String primaryType,
        GeoJsonPoint location,
        WeightedRating rating,
        Double distance

) {

}
