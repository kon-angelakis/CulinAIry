package com.kangel.thesis.aipowered_location_advisor.Models.Records;

public record ReviewAdditionRequest(
                String placeId,
                String placeName,
                String text,
                Integer rating) {

}
