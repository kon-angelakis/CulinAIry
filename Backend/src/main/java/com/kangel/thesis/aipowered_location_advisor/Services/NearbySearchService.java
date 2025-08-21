package com.kangel.thesis.aipowered_location_advisor.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.kangel.thesis.aipowered_location_advisor.Models.Place;
import com.kangel.thesis.aipowered_location_advisor.Models.Review;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.NearbySearchRequest;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.NearbySearchResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.PlaceDetails;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.PlaceDetails.PhotoDTO;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.PlaceDetails.ReviewDTO;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class NearbySearchService {

    private final String API_KEY;
    private WebClient webClient;

    public NearbySearchService(Dotenv env) {
        this.API_KEY = env.get("MAPS_PLATFORM_KEY");
        // default webclient parameters build
        webClient = WebClient.builder()
                .codecs(config -> config.defaultCodecs().maxInMemorySize(1024 * 1024))
                .baseUrl("https://places.googleapis.com")
                .build();
    }

    // Returns a list of up to 20 places
    public List<Place> NearbySearch(NearbySearchRequest request) {
        List<Place> results = new ArrayList<>();
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
                request.restaurant_types(),
                request.num_of_places(),
                request.location().latitude(),
                request.location().longitude(),
                request.radius());

        NearbySearchResponse response = webClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/places:searchNearby")
                        .queryParam("key", API_KEY)
                        .queryParam("fields",
                                "places.id,places.displayName,places.primaryTypeDisplayName,places.types,places.internationalPhoneNumber,places.location,places.formattedAddress,places.rating,places.userRatingCount,places.websiteUri,places.regularOpeningHours,places.photos,places.reviews,places.googleMapsLinks")
                        .build())
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(NearbySearchResponse.class)
                .block();

        // Through the dto record of places extract each one as a place object and add
        // it to the list
        for (PlaceDetails placeDTO : response.places()) {
            results.add(ExtractPlaceData(placeDTO));
        }
        return results;
    }

    // Used for updating place data if the dateUpdated is outdated(currently 2
    // weeks)
    public Place SearchPlaceDetails(String id) {
        PlaceDetails response = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/places/{id}")
                        .queryParam("key", API_KEY)
                        .queryParam("fields",
                                "id,displayName,primaryTypeDisplayName,types,internationalPhoneNumber,location,formattedAddress,rating,userRatingCount,websiteUri,regularOpeningHours,photos,reviews,googleMapsLinks")
                        .build(id))
                .retrieve()
                .bodyToMono(PlaceDetails.class)
                .block();

        return ExtractPlaceData(response);
    }

    private Place ExtractPlaceData(PlaceDetails placeDTO) { // From google's nearby search api response extract and keep
                                                            // valuable fields as a Place object

        GeoJsonPoint location = new GeoJsonPoint(
                placeDTO.location().longitude(),
                placeDTO.location().latitude());
        List<String> secondaryTypes = placeDTO.types();
        List<String> schedule = placeDTO.regularOpeningHours() != null
                ? placeDTO.regularOpeningHours().weekdayDescriptions()
                : List.of();
        List<String> photos = placeDTO.photos() != null
                ? placeDTO.photos().stream().map(PhotoDTO::toPhotoString).toList()
                : List.of();
        List<Review> reviews = placeDTO.reviews() != null
                ? placeDTO.reviews().stream().map(ReviewDTO::toReview).toList()
                : List.of();

        return (new Place(
                placeDTO.id(),
                placeDTO.displayName().text(),
                placeDTO.primaryTypeDisplayName().text(),
                placeDTO.internationalPhoneNumber(),
                placeDTO.formattedAddress(),
                placeDTO.websiteUri(),
                placeDTO.googleMapsLinks().directionsUri(),
                placeDTO.rating(),
                placeDTO.userRatingCount(),
                location,
                secondaryTypes,
                schedule,
                photos,
                reviews,
                LocalDateTime.now()));
    }
}
