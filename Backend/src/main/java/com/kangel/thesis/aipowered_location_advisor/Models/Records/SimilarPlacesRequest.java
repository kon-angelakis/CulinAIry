package com.kangel.thesis.aipowered_location_advisor.Models.Records;

import java.util.List;

public record SimilarPlacesRequest(
                Location location,
                String primaryType,
                List<String> secondaryTypes,
                String originalId) {

}
