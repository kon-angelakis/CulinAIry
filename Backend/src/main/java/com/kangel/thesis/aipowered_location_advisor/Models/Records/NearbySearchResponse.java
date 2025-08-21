package com.kangel.thesis.aipowered_location_advisor.Models.Records;

import java.util.List;

public record NearbySearchResponse(
        List<PlaceDetails> places) {
}