package com.kangel.thesis.aipowered_location_advisor.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.kangel.thesis.aipowered_location_advisor.Models.Place;
import com.kangel.thesis.aipowered_location_advisor.Models.Review;

@Service
public class GPlacesService {

    public Map<String, Object> NearbySearch(Map<String, Object> payload) {
        Map<String, Object> location = (Map<String, Object>) payload.get("location");
        String requestBody = String.format("""
        {
            "includedTypes": %s,
            "maxResultCount": %d,
            "locationRestriction": {
                "circle": {
                    "center": {
                        "latitude": %s,
                        "longitude":  %s
                    },
                    "radius": %s
                }
            }
        }""", 
            payload.get("restaurant_types"), 
            10,
            location.get("latitude").toString(),
            location.get("longitude").toString(),
            payload.get("radius").toString()                
            );
        System.out.println(requestBody);   
        WebClient webClient = WebClient.builder()
        .codecs(config -> config.defaultCodecs().maxInMemorySize(1024 * 1024))
        .build();
        Map<String, Object> response = webClient
        .post()
        .uri("https://places.googleapis.com/v1/places:searchNearby")
        .header("Content-Type", "application/json")
        .header("X-Goog-Api-Key", "AIzaSyA3lskh6zjK3tU1QYKMDihaNZY8qg8WKy4")
        .header("X-Goog-FieldMask", "places.id,places.displayName,places.primaryTypeDisplayName,places.types,places.internationalPhoneNumber,places.location,places.formattedAddress,places.rating,places.userRatingCount,places.websiteUri,places.regularOpeningHours,places.photos,places.reviews,places.googleMapsLinks")
        .bodyValue(requestBody)
        .retrieve()
        .bodyToMono(Map.class)
        .block(); 
        return response;
    }

    public Place ExtractPlaceData(Map<String, Object> place){ //From google's nearby seaerch api response extract and keep valuable fields as a Place object
        String id = (String) place.get("id");
        Double rating = (Double) place.get("rating");
        String phone = (String) place.get("internationalPhoneNumber");
        String address = (String) place.get("formattedAddress");
        String website = (String) place.get("websiteUri");
        int ratingCount = (int) place.get("userRatingCount");
        Map<String, Object> displayNameRaw = (Map<String, Object>) place.get("displayName");
        Map<String, Object> primaryTypeDisplayNameRaw = (Map<String, Object>) place.get("primaryTypeDisplayName");
        Map<String, Object> googleLinksRaw = (Map<String, Object>) place.get("googleMapsLinks");
        Map<String, Object> locationRaw = (Map<String, Object>) place.get("location");
        Map<String, Object> scheduleRaw = (Map<String, Object>) place.get("regularOpeningHours");
        List<Map<String, Object>> reviewsRaw = (List<Map<String, Object>>) place.get("reviews");
        List<Map<String, Object>> photosRaw = (List<Map<String, Object>>) place.get("photos");

        String displayName = (String) displayNameRaw.get("text");
        String restaurantType = (String) primaryTypeDisplayNameRaw.get("text");
        String directionsLink = (String) googleLinksRaw.get("directionsUri");
        GeoJsonPoint location = new GeoJsonPoint((double)locationRaw.get("longitude"),(double)locationRaw.get("latitude"));
        ArrayList<String> secondaryTypes = (ArrayList<String>) place.get("types");
        ArrayList<String> schedule = (ArrayList<String>)scheduleRaw.get("weekdayDescriptions");

        ArrayList<Review> reviews = new ArrayList<Review>();
        ArrayList<String> photos = new ArrayList<String>();
        
        for (Map<String, Object> review : reviewsRaw) {
            Integer reviewRating = (Integer) review.get("rating");
            Map<String, Object> text = (Map<String, Object>) review.get("text");
            String reviewText = (String) text.get("text");
            reviews.add(new Review(reviewRating, reviewText));
        }

        for (Map<String, Object> photo : photosRaw) {
            String photoID = photo.get("name").toString().replace("places/" + id + "/photos/", "");
            photos.add(photoID);
        }

        return(new Place(id, displayName, restaurantType, phone, address, website, directionsLink, rating, ratingCount, location, secondaryTypes, schedule, photos, reviews));
    }
}
