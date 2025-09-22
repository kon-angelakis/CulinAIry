package com.kangel.thesis.aipowered_location_advisor.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kangel.thesis.aipowered_location_advisor.Models.Place;
import com.kangel.thesis.aipowered_location_advisor.Models.Review;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ApiResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.PlaceDTO;
import com.kangel.thesis.aipowered_location_advisor.Repositories.PlaceRepo;

@Service
public class PlaceService {

    public static final int MAX_TIME_TO_UPDATE_IN_WEEKS = 2;

    private final PlaceRepo placeRepo;
    private final NearbySearchService nearbySearchService;
    private final ReviewService reviewService;

    public PlaceService(PlaceRepo placeRepo, NearbySearchService nearbySearchService, ReviewService reviewService) {
        this.placeRepo = placeRepo;
        this.nearbySearchService = nearbySearchService;
        this.reviewService = reviewService;

    }

    public ApiResponse<Place> FindPlace(String id) {
        Optional<Place> placeFound = placeRepo.findById(id);
        if (!placeFound.isPresent())
            return new ApiResponse<Place>(false, "No place data found", null);

        Place place = placeFound.get();
        LocalDateTime timeToUpdate = LocalDateTime.now().minusWeeks(MAX_TIME_TO_UPDATE_IN_WEEKS);

        if (!place.isDetailed() || place.getDateUpdated().isBefore(timeToUpdate))
            place = SavePlace(nearbySearchService.DetailedSearch(place.getId(), place));
        return new ApiResponse<Place>(true, "Place data retrieved", place);
    }

    public ApiResponse<List<Review>> FindReviews(String id) {
        Optional<Place> placeFound = placeRepo.findById(id);
        if (!placeFound.isPresent())
            return new ApiResponse<List<Review>>(false, "No place data found", null);
        List<Review> reviews = reviewService.FindByPlaceId(id);
        if (reviews.size() < 6) // Grab reviews from google
            reviews = nearbySearchService.ReviewsSearch(id);
        return new ApiResponse<List<Review>>(true, "Place reviews retrieved", reviews);
    }

    public List<Place> FindPlacesDemanding(double lon, double lat, int maxDist, List<String> types) {
        return placeRepo.findNearbyPlacesDemanding(lon, lat, maxDist, types);
    }

    public List<Place> FindPlaceInclusive(double lon, double lat, int maxDist, List<String> types) {
        return placeRepo.findNearbyPlacesInclusive(lon, lat, maxDist, types);
    }

    public List<PlaceDTO> FindAllPlaceDTOSById(Iterable<String> ids) {
        return placeRepo.findAllPlaceDTOSById(ids);
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

}
