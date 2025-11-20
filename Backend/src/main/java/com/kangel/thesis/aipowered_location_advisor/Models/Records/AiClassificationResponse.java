package com.kangel.thesis.aipowered_location_advisor.Models.Records;

import java.util.List;

public record AiClassificationResponse(
                List<String> restaurantTypes,
                List<String> moodTypes,
                String userIntent) {

}
