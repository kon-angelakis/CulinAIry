package com.kangel.thesis.aipowered_location_advisor.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.kangel.thesis.aipowered_location_advisor.Models.Place;
import com.kangel.thesis.aipowered_location_advisor.Models.Review;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.NearbySearchRequest;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.NearbySearchResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.PlaceDetails;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.PlaceImageResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.PlaceDetails.PhotoDTO;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.PlaceDetails.ReviewDTO;

import io.github.cdimascio.dotenv.Dotenv;
import io.imagekit.sdk.exceptions.BadRequestException;
import io.imagekit.sdk.exceptions.ForbiddenException;
import io.imagekit.sdk.exceptions.InternalServerException;
import io.imagekit.sdk.exceptions.TooManyRequestsException;
import io.imagekit.sdk.exceptions.UnauthorizedException;
import io.imagekit.sdk.exceptions.UnknownException;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class NearbySearchService {

    private final ImagekitService imagekitService;
    private final String API_KEY;
    private WebClient webClient;

    public NearbySearchService(Dotenv env, ImagekitService imagekitService) {
        this.imagekitService = imagekitService;
        this.API_KEY = env.get("MAPS_PLATFORM_KEY");
        // default webclient parameters build
        webClient = WebClient.builder()
                .codecs(config -> config.defaultCodecs().maxInMemorySize(1024 * 1024))
                .baseUrl("https://places.googleapis.com")
                .build();
    }

    // Returns a list of up to 20 places
    public List<Place> NearbySearch(@Valid NearbySearchRequest request) {
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
        if (response != null && response.places() != null) {
            for (PlaceDetails placeDTO : response.places()) {
                results.add(ExtractPlaceData(placeDTO));
            }
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

    private Mono<String> ExtractAndUploadPlaceImageAsync(String placeId, String photoId, int photoNum) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/places/{placeId}/photos/{photoId}/media")
                        .queryParam("maxHeightPx", 1080)
                        .queryParam("maxWidthPx", 1920)
                        .queryParam("skipHttpRedirect", true)
                        .queryParam("key", API_KEY)
                        .build(placeId, photoId))
                .retrieve()
                .bodyToMono(PlaceImageResponse.class)
                .flatMap(response -> Mono
                        .fromCallable(() -> imagekitService.UploadImage(response.photoUri(), placeId, photoNum)))
                .map(uploaded -> imagekitService.RequestImage(placeId, photoNum));
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
        // Contains imageIds
        List<String> photosRaw = placeDTO.photos() != null
                ? placeDTO.photos().stream().limit(3).map(PhotoDTO::toPhotoString).toList()
                : List.of();

        // Contains imagekit links for image retrieval
        List<String> photosRefined = Flux.fromIterable(photosRaw)
                .flatMap(photoId -> ExtractAndUploadPlaceImageAsync(placeDTO.id(), photoId, photosRaw.indexOf(photoId)))
                .collectList()
                .block(); // Only block once for the whole list

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
                photosRefined,
                reviews,
                LocalDateTime.now()));
    }
}
