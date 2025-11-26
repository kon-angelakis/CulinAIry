package com.kangel.thesis.aipowered_location_advisor.Models.Records;

public record PaginatedResponse<T>(
                T content,
                int totalElements,
                int totalPages) {

}
