package com.kangel.thesis.aipowered_location_advisor.Models.Records;

import com.kangel.thesis.aipowered_location_advisor.Models.Enums.InteractionType;

public record UserPlacesRequest(
        InteractionType type,
        Location location,
        PaginationRequest pagingRequest) {

}
