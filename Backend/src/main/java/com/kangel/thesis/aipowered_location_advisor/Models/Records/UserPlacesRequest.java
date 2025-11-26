package com.kangel.thesis.aipowered_location_advisor.Models.Records;

import org.springframework.data.domain.Sort;

import com.kangel.thesis.aipowered_location_advisor.Models.Enums.InteractionType;

public record UserPlacesRequest(
                InteractionType type,
                Location location,
                PaginationRequest pagingRequest,
                Sort.Direction sortOrder) {

}
