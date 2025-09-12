package com.kangel.thesis.aipowered_location_advisor.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kangel.thesis.aipowered_location_advisor.Models.Place;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ApiResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.PlaceDTO;
import com.kangel.thesis.aipowered_location_advisor.Repositories.PlaceRepo;

@Service
public class PlaceService {

    private final PlaceRepo placeRepo;

    public PlaceService(PlaceRepo placeRepo) {
        this.placeRepo = placeRepo;
    }

    public ApiResponse<Place> FindPlace(String id) {
        Optional<Place> placeFound = placeRepo.findById(id);
        if (!placeFound.isPresent())
            return new ApiResponse<Place>(false, "No place data found", null);
        return new ApiResponse<Place>(true, "Place data retrieved", placeFound.get());
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
