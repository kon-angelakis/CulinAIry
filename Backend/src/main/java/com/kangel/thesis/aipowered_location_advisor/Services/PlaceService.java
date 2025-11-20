package com.kangel.thesis.aipowered_location_advisor.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kangel.thesis.aipowered_location_advisor.Models.Place;
import com.kangel.thesis.aipowered_location_advisor.Models.Review;
import com.kangel.thesis.aipowered_location_advisor.Models.UserInteraction;
import com.kangel.thesis.aipowered_location_advisor.Models.Enums.InteractionType;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ApiResponse;
import com.kangel.thesis.aipowered_location_advisor.Repositories.PlaceRepo;

@Service
public class PlaceService {

    private final PlaceRepo placeRepo;
    private final NearbySearchService nearbySearchService;
    private final ReviewService reviewService;
    private final UserInteractionService interactionService;

    public PlaceService(PlaceRepo placeRepo, NearbySearchService nearbySearchService, ReviewService reviewService,
            UserInteractionService interactionService) {
        this.placeRepo = placeRepo;
        this.nearbySearchService = nearbySearchService;
        this.reviewService = reviewService;
        this.interactionService = interactionService;

    }

    public ApiResponse<Place> FindPlace(String id) {
        Optional<Place> placeFound = placeRepo.findById(id);
        if (!placeFound.isPresent())
            return new ApiResponse<Place>(false, "No place data found", null);

        Place place = placeFound.get();

        return new ApiResponse<Place>(true, "Place data retrieved", place);
    }

    public ApiResponse<List<Review>> FindReviews(String id) {
        Optional<Place> placeFound = placeRepo.findById(id);
        if (!placeFound.isPresent())
            return new ApiResponse<List<Review>>(false, "No place data found", null);
        Place place = placeFound.get();
        List<Review> reviews = reviewService.FindByPlaceId(id);
        if (!place.isGoogleReviewed()) { // Grab reviews from google once
            reviews = nearbySearchService.ReviewsSearch(id);
            place.setGoogleReviewed(true);
            SavePlace(place);
        }
        return new ApiResponse<List<Review>>(true, "Place reviews retrieved", reviews);
    }

    public List<Place> FindPlacesDemanding(double lon, double lat, int maxDist, List<String> types) {
        return placeRepo.findPlacesDemandingNearby(lon, lat, maxDist, types);
    }

    public List<Place> FindPlaceInclusive(double lon, double lat, int maxDist, List<String> types) {
        return placeRepo.findPlacesInclusiveNearby(lon, lat, maxDist, types);
    }

    public List<Place> FindAllPlacesById(List<String> ids, double lon, double lat) {
        return placeRepo.findByIdIn(ids, lon, lat);
    }

    public List<Place> FindAllPlacesByIdNearby(List<String> ids, double lon, double lat) {
        return placeRepo.findByIdInNearby(ids, lon, lat);
    }

    public List<Place> FindTopPlaces(double lon, double lat) {
        return placeRepo.findTopPlacesNearby(lon, lat);
    }

    public List<Place> FindSimilarPlaces(double lon, double lat, String primaryType, List<String> secondaryTypes,
            String originalId) {
        return placeRepo.findSimilarPlacesNearby(lon, lat, primaryType, secondaryTypes, originalId);
    }

    public Place SavePlace(Place place) {
        if (place == null)
            return null;
        return placeRepo.save(place);
    }

    public List<Place> SavePlaces(List<Place> places) {
        if (places == null)
            return null;
        return placeRepo.saveAll(places);
    }

    public ApiResponse<Integer> FindTimesFavourited(String placeId) {
        List<UserInteraction> favInteractions = interactionService.FindAllByPlaceIdAndType(placeId,
                InteractionType.FAVOURITES);
        return new ApiResponse<Integer>(true, "Retrieved place favourited count", favInteractions.size());
    }

}
