package com.kangel.thesis.aipowered_location_advisor.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kangel.thesis.aipowered_location_advisor.Models.Place;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ApiResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.PlaceDTO;
import com.kangel.thesis.aipowered_location_advisor.Repositories.PlaceRepo;

@Service
public class PlaceService {

    public static final int MAX_TIME_TO_UPDATE_IN_WEEKS = 2;

    private final PlaceRepo placeRepo;
    private final NearbySearchService nearbySearchService;

    public PlaceService(PlaceRepo placeRepo, NearbySearchService nearbySearchService) {
        this.placeRepo = placeRepo;
        this.nearbySearchService = nearbySearchService;
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

    public ApiResponse<Place> FindReviews(String id) {
        Optional<Place> placeFound = placeRepo.findById(id);
        if (!placeFound.isPresent())
            return new ApiResponse<Place>(false, "No place data found", null);
        Place place = placeFound.get();
        place = SavePlace(nearbySearchService.ReviewsSearch(place.getId(), place));
        return new ApiResponse<Place>(true, "Place reviews retrieved", place);
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
