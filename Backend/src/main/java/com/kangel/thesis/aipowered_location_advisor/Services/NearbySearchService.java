package com.kangel.thesis.aipowered_location_advisor.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.kangel.thesis.aipowered_location_advisor.Models.Place;
import com.kangel.thesis.aipowered_location_advisor.Models.Review;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.GooglePlaceDetails;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.GooglePlaceDetails.PhotoDTO;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.NearbySearchRequest;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.NearbySearchResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.PlaceImageResponse;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class NearbySearchService {

        private final ImagekitService imagekitService;
        private final ReviewService reviewService;
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

        public NearbySearchService(Dotenv env, ImagekitService imagekitService, ReviewService reviewService) {
                this.imagekitService = imagekitService;
                this.reviewService = reviewService;
                this.API_KEY = env.get("MAPS_PLATFORM_KEY");
                // default webclient parameters build
                webClient = WebClient.builder()
                                .codecs(config -> config.defaultCodecs().maxInMemorySize(1024 * 1024))
                                .baseUrl("https://places.googleapis.com")
                                .build();
        }

        // Triggers just the Pro SKU 1 time per N results (5000 free req/month)
        // Triggers Photo Details SKU N times per N results (1000 free req/month)
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
                                                                "places.id,places.displayName,places.primaryType,places.primaryTypeDisplayName,places.types,places.location,places.formattedAddress,places.photos,places.googleMapsLinks,places.internationalPhoneNumber,places.rating,places.userRatingCount,places.websiteUri,places.regularOpeningHours")
                                                .build())
                                .bodyValue(requestBody)
                                .retrieve()
                                .bodyToMono(NearbySearchResponse.class)
                                .block();

                // Through the dto record of place detals extract each one as a place object and
                // add it to the list
                if (response != null && response.places() != null) {
                        for (GooglePlaceDetails placeDetails : response.places()) {
                                Place toAdd = ExtractPlaceData(placeDetails,
                                                String.format("places/%s/thumbnail", placeDetails.id()), 1);
                                if (toAdd != null)
                                        results.add(toAdd);
                        }
                }
                return results;
        }

        // Triggers Photo Details SKU 3*N times per N results (1000 free req/month)
        public Place PhotoSearch(String id, Place place) {
                GooglePlaceDetails response = webClient
                                .get()
                                .uri(uriBuilder -> uriBuilder
                                                .path("/v1/places/{id}")
                                                .queryParam("key", API_KEY)
                                                .queryParam("fields",
                                                                "id,photos")
                                                .build(id))
                                .retrieve()
                                .bodyToMono(GooglePlaceDetails.class)
                                .block();

                return ExtractPlacePhotos(response, place, String.format("places/%s/slideshow", id), 3);
        }

        // Used to retrieve google place reviews if the user wants
        // Triggers Enterprise & Atmosphere SKU 1 times per N results (1000 free
        // req/month)
        public List<Review> ReviewsSearch(String id) {
                GooglePlaceDetails response = webClient
                                .get()
                                .uri(uriBuilder -> uriBuilder
                                                .path("/v1/places/{id}")
                                                .queryParam("key", API_KEY)
                                                .queryParam("fields",
                                                                "id,reviews")
                                                .build(id))
                                .retrieve()
                                .bodyToMono(GooglePlaceDetails.class)
                                .block();

                return (ExtractPlaceReviewData(response, id));
        }

        private Mono<String> ExtractAndUploadPlaceImageAsync(String placeId, String photoId, String location,
                        Integer photoNum) {
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
                                                .fromCallable(() -> imagekitService.UploadImageString(
                                                                response.photoUri(),
                                                                location, Integer.toString(photoNum))))
                                // Google changes image name so save a number instead
                                .map(uploaded -> imagekitService.RequestImage(location,
                                                Integer.toString(photoNum)));
        }

        // Extract api fields and store them as place objects
        private Place ExtractPlaceData(GooglePlaceDetails placeDetails, String photosLocation,
                        int totalPhotos) {

                // If the place's primary type is food related only then return it else discard
                // it, some google maps owners apparently love to for example add bar and night
                // club to a hairdresser salon
                String type = placeDetails.primaryType() != null ? placeDetails.primaryType() : null;
                String name = placeDetails.displayName() != null ? placeDetails.displayName().text() : null;
                if (!acceptablePrimaryTypes.contains(type) || name == null)
                        return null;

                String id = placeDetails.id();

                String address = placeDetails.formattedAddress() != null ? placeDetails.formattedAddress() : null;
                String typeDisplayName = placeDetails.primaryTypeDisplayName() != null
                                ? placeDetails.primaryTypeDisplayName().text()
                                : null;
                String directions = placeDetails.googleMapsLinks() != null
                                ? placeDetails.googleMapsLinks().directionsUri()
                                : null;
                GeoJsonPoint location = placeDetails.location() != null ? new GeoJsonPoint(
                                placeDetails.location().longitude(),
                                placeDetails.location().latitude()) : null;

                // secondary types may contain non food related well.. types so intersect with
                // the list of all acceptable types
                List<String> secondaryTypes = !placeDetails.types().isEmpty()
                                ? placeDetails.types()
                                : null;
                if (secondaryTypes != null)
                        secondaryTypes.retainAll(acceptablePrimaryTypes);

                // Contains imageIds
                List<String> photosRaw = placeDetails.photos() != null
                                ? placeDetails.photos().stream().limit(totalPhotos).map(PhotoDTO::toPhotoString)
                                                .toList()
                                : null;

                // Contains imagekit links for image retrieval
                List<String> photosRefined = photosRaw != null && !photosRaw.isEmpty() ? Flux.fromIterable(photosRaw)
                                .flatMap(photoId -> ExtractAndUploadPlaceImageAsync(placeDetails.id(), photoId,
                                                photosLocation,
                                                photosRaw.indexOf(photoId)))
                                .collectList()
                                .block() // Only block once for the whole list
                                : null;
                String thumbnail = photosRefined != null ? photosRefined.get(0) : null;

                Double rating = placeDetails.rating() != null ? placeDetails.rating() : 0;
                String phone = placeDetails.internationalPhoneNumber() != null ? placeDetails.internationalPhoneNumber()
                                : null;
                String website = placeDetails.websiteUri() != null ? placeDetails.websiteUri() : null;
                Integer totalRatings = placeDetails.userRatingCount() != null ? placeDetails.userRatingCount() : 0;

                List<String> schedule = placeDetails.regularOpeningHours() != null
                                ? placeDetails.regularOpeningHours().weekdayDescriptions()
                                : null;

                return (new Place(id, true, false, thumbnail, name, typeDisplayName, type, phone, address, website,
                                directions, rating, 0.0, totalRatings, 0,
                                location, null, secondaryTypes, schedule, null, null,
                                LocalDateTime.now()));
        }

        private Place ExtractPlacePhotos(GooglePlaceDetails placeDetails, Place place,
                        String photosLocation,
                        int totalPhotos) {

                String id = placeDetails.id();

                // Contains imageIds
                List<String> photosRaw = placeDetails.photos() != null
                                ? placeDetails.photos().stream().limit(totalPhotos).map(PhotoDTO::toPhotoString)
                                                .toList()
                                : null;

                // Contains imagekit links for image retrieval
                List<String> photosRefined = photosRaw != null && !photosRaw.isEmpty() ? Flux.fromIterable(photosRaw)
                                .flatMap(photoId -> ExtractAndUploadPlaceImageAsync(placeDetails.id(), photoId,
                                                photosLocation,
                                                photosRaw.indexOf(photoId)))
                                .collectList()
                                .block() // Only block once for the whole list
                                : null;

                return (new Place(id, true, false, place.getThumbnail(), place.getName(), place.getPrimaryType(),
                                place.getPrimaryTypeRaw(), place.getPhone(),
                                place.getAddress(), place.getWebsite(),
                                place.getDirectionsUri(), place.getRating(), place.getInappRating(),
                                place.getTotalRatings(),
                                place.getInappTotalRatings(),
                                place.getLocation(), place.getDistance(), place.getSecondaryTypes(),
                                place.getSchedule(),
                                photosRefined,
                                null,
                                LocalDateTime.now()));
        }

        private List<Review> ExtractPlaceReviewData(GooglePlaceDetails placeDetails, String placeId) {
                List<Review> reviews = placeDetails.reviews() != null
                                ? placeDetails.reviews().stream().map(dto -> {
                                        Review r = dto.toReview();
                                        r.setPlaceId(placeId);
                                        return r;
                                }).toList()
                                : null;
                reviews = reviewService.SaveReviews(reviews);
                return reviews;
        }
}
