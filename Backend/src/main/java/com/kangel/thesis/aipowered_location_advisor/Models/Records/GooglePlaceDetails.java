package com.kangel.thesis.aipowered_location_advisor.Models.Records;

import java.util.List;

import com.kangel.thesis.aipowered_location_advisor.Models.Review;

public record GooglePlaceDetails(
                String id,
                Double rating,
                String internationalPhoneNumber,
                String formattedAddress,
                String websiteUri,
                Integer userRatingCount,
                DisplayName displayName,
                String primaryType, // eg greek_restaurant
                PrimaryTypeDisplayName primaryTypeDisplayName, // eg Greek Restaurant
                GoogleLinks googleMapsLinks,
                Location location,
                List<String> types,
                RegularOpeningHours regularOpeningHours,
                List<ReviewDTO> reviews,
                List<PhotoDTO> photos

) {
        public record DisplayName(
                        String text) {
        }

        public record PrimaryTypeDisplayName(
                        String text) {
        }

        public record GoogleLinks(
                        String directionsUri,
                        String placeUri,
                        String writeAReviewUri,
                        String reviewsUri,
                        String photosUri) {
        }

        public record RegularOpeningHours(
                        List<String> weekdayDescriptions) {
        }

        public record ReviewDTO(
                        int rating,
                        TextDTO text) {
                public Review toReview() {
                        return new Review(rating, text != null ? text.text() : null);
                }
        }

        public record TextDTO(
                        String text,
                        String languageCode) {
        }

        public record PhotoDTO(
                        String name) {
                public String toPhotoString() {
                        return name.replaceFirst(".*photos/", "");
                }
        }
}
