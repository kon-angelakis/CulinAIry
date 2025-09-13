package com.kangel.thesis.aipowered_location_advisor.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.kangel.thesis.aipowered_location_advisor.Models.Place;
import com.kangel.thesis.aipowered_location_advisor.Models.Review;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.NearbySearchRequest;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.NearbySearchResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.GooglePlaceDetails;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.GooglePlaceDetails.PhotoDTO;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.GooglePlaceDetails.ReviewDTO;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.PlaceImageResponse;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class NearbySearchService {

        private final ImagekitService imagekitService;
        private final String API_KEY;
        private WebClient webClient;

        // Food related place types
        private final List<String> acceptablePrimaryTypes = List.of(
                        "acai_shop",
                        "afghani_restaurant",
                        "african_restaurant",
                        "american_restaurant",
                        "asian_restaurant",
                        "bagel_shop",
                        "bakery",
                        "bar",
                        "bar_and_grill",
                        "barbecue_restaurant",
                        "brazilian_restaurant",
                        "breakfast_restaurant",
                        "brunch_restaurant",
                        "buffet_restaurant",
                        "cafe",
                        "cafeteria",
                        "candy_store",
                        "cat_cafe",
                        "chinese_restaurant",
                        "chocolate_factory",
                        "chocolate_shop",
                        "coffee_shop",
                        "confectionery",
                        "deli",
                        "dessert_restaurant",
                        "dessert_shop",
                        "diner",
                        "dog_cafe",
                        "donut_shop",
                        "fast_food_restaurant",
                        "fine_dining_restaurant",
                        "food_court",
                        "french_restaurant",
                        "greek_restaurant",
                        "hamburger_restaurant",
                        "ice_cream_shop",
                        "indian_restaurant",
                        "indonesian_restaurant",
                        "italian_restaurant",
                        "japanese_restaurant",
                        "juice_shop",
                        "korean_restaurant",
                        "lebanese_restaurant",
                        "meal_delivery",
                        "meal_takeaway",
                        "mediterranean_restaurant",
                        "mexican_restaurant",
                        "middle_eastern_restaurant",
                        "pizza_restaurant",
                        "pub",
                        "ramen_restaurant",
                        "restaurant",
                        "sandwich_shop",
                        "seafood_restaurant",
                        "spanish_restaurant",
                        "steak_house",
                        "sushi_restaurant",
                        "tea_house",
                        "thai_restaurant",
                        "turkish_restaurant",
                        "vegan_restaurant",
                        "vegetarian_restaurant",
                        "vietnamese_restaurant",
                        "wine_bar");

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
                                request.restaurantTypes(),
                                request.numOfPlaces(),
                                request.location().latitude(),
                                request.location().longitude(),
                                request.radius());
                NearbySearchResponse response = webClient
                                .post()
                                .uri(uriBuilder -> uriBuilder
                                                .path("/v1/places:searchNearby")
                                                .queryParam("key", API_KEY)
                                                .queryParam("fields",
                                                                "places.id,places.displayName,places.primaryType,places.primaryTypeDisplayName,places.types,places.internationalPhoneNumber,places.location,places.formattedAddress,places.rating,places.userRatingCount,places.websiteUri,places.regularOpeningHours,places.photos,places.reviews,places.googleMapsLinks")
                                                .build())
                                .bodyValue(requestBody)
                                .retrieve()
                                .bodyToMono(NearbySearchResponse.class)
                                .block();

                // Through the dto record of places extract each one as a place object and add
                // it to the list
                if (response != null && response.places() != null) {
                        for (GooglePlaceDetails placeDetails : response.places()) {
                                Place toAdd = ExtractPlaceData(placeDetails);
                                if (toAdd != null)
                                        results.add(ExtractPlaceData(placeDetails));
                        }
                }
                return results;
        }

        // Used for updating place data if the dateUpdated is outdated(currently 2
        // weeks)
        public Place SearchPlaceDetails(String id) {
                GooglePlaceDetails response = webClient
                                .get()
                                .uri(uriBuilder -> uriBuilder
                                                .path("/v1/places/{id}")
                                                .queryParam("key", API_KEY)
                                                .queryParam("fields",
                                                                "id,displayName,primaryType,primaryTypeDisplayName,types,internationalPhoneNumber,location,formattedAddress,rating,userRatingCount,websiteUri,regularOpeningHours,photos,reviews,googleMapsLinks")
                                                .build(id))
                                .retrieve()
                                .bodyToMono(GooglePlaceDetails.class)
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
                                                .fromCallable(() -> imagekitService.UploadImage(response.photoUri(),
                                                                placeId, photoNum)))
                                .map(uploaded -> imagekitService.RequestImage(placeId, photoNum));
        }

        private Place ExtractPlaceData(GooglePlaceDetails placeDetails) { // From google's nearby search api response
                                                                          // extract
                                                                          // and
                                                                          // keep
                                                                          // valuable fields as a Place object

                // If the place's primary type is food related only then return it else discard
                // it, some google maps owners apparently love to for example add bar and night
                // club to a hairdresser salon
                String type = placeDetails.primaryType() != null ? placeDetails.primaryType() : "-";
                String name = placeDetails.displayName() != null ? placeDetails.displayName().text() : null;
                if (!acceptablePrimaryTypes.contains(type) || name == null)
                        return null;

                String id = placeDetails.id();
                Double rating = placeDetails.rating() != null ? placeDetails.rating() : 0;
                String phone = placeDetails.internationalPhoneNumber() != null ? placeDetails.internationalPhoneNumber()
                                : "-";
                String address = placeDetails.formattedAddress() != null ? placeDetails.formattedAddress() : "-";
                String website = placeDetails.websiteUri() != null ? placeDetails.websiteUri() : "-";
                Integer totalRatings = placeDetails.userRatingCount() != null ? placeDetails.userRatingCount() : 0;
                String typeDisplayName = placeDetails.primaryTypeDisplayName() != null
                                ? placeDetails.primaryTypeDisplayName().text()
                                : "-";
                String directions = placeDetails.googleMapsLinks() != null
                                ? placeDetails.googleMapsLinks().directionsUri()
                                : "-";
                GeoJsonPoint location = placeDetails.location() != null ? new GeoJsonPoint(
                                placeDetails.location().longitude(),
                                placeDetails.location().latitude()) : null;

                // secondary types may contain non food related well.. types so intersect with
                // the list of all acceptable types
                List<String> secondaryTypes = !placeDetails.types().isEmpty()
                                ? placeDetails.types()
                                : List.of();
                secondaryTypes.retainAll(acceptablePrimaryTypes);
                List<String> schedule = placeDetails.regularOpeningHours() != null
                                ? placeDetails.regularOpeningHours().weekdayDescriptions()
                                : List.of();

                // Contains imageIds
                List<String> photosRaw = placeDetails.photos() != null
                                ? placeDetails.photos().stream().limit(2).map(PhotoDTO::toPhotoString).toList()
                                : List.of();

                // Contains imagekit links for image retrieval
                List<String> photosRefined = !photosRaw.isEmpty() ? Flux.fromIterable(photosRaw)
                                .flatMap(photoId -> ExtractAndUploadPlaceImageAsync(placeDetails.id(), photoId,
                                                photosRaw.indexOf(photoId)))
                                .collectList()
                                .block() // Only block once for the whole list
                                : List.of();

                String thumbnail = !photosRefined.isEmpty()
                                ? photosRefined.get(new Random().nextInt(photosRefined.size()))
                                : "-";

                List<Review> reviews = placeDetails.reviews() != null
                                ? placeDetails.reviews().stream().map(ReviewDTO::toReview).toList()
                                : List.of();

                return (new Place(id, thumbnail, name, typeDisplayName, phone, address, website,
                                directions, rating, totalRatings,
                                location, secondaryTypes, schedule, photosRefined, reviews,
                                LocalDateTime.now()));
        }
}
