package com.kangel.thesis.aipowered_location_advisor.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kangel.thesis.aipowered_location_advisor.Models.Place;
import com.kangel.thesis.aipowered_location_advisor.Models.Enums.AiProvider;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.AiClassificationResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.AiRequest;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ApiResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.Location;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.NearbySearchRequest;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.PlaceDTO;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.SearchRequest;

import jakarta.validation.Valid;

@Service
public class SearchService {

    public static final int MAX_NEARBY_SEARCH_RESULTS = 1;

    private final AiManager aiManager;
    private final NearbySearchService nearSearchService;
    private final PlaceService placeService;

    public SearchService(AiManager aiManager, NearbySearchService nearSearchService, PlaceService placeService) {
        this.aiManager = aiManager;
        this.nearSearchService = nearSearchService;
        this.placeService = placeService;
    }

    public ApiResponse<LinkedHashSet<PlaceDTO>> SearchPlaces(@Valid SearchRequest request)
            throws JsonProcessingException, InterruptedException {
        try {

            AiClassificationResponse aiResponse = ClassifyUserInput(request.userInput()).data();
            List<String> restaurantTypes = aiResponse.restaurantTypes();
            String userIntent = aiResponse.userIntent();
            Location coordinates = request.location();

            Set<Place> recommendedPlaces = FetchPlacesFromDB(coordinates, request.radius(), restaurantTypes,
                    userIntent);

            // Fetch additional places if less than MAX_NEARBY_SEARCH_RESULTS
            if (recommendedPlaces.size() < MAX_NEARBY_SEARCH_RESULTS) {
                FetchNewPlaces(recommendedPlaces, restaurantTypes, coordinates,
                        request.radius());
            }

            placeService.SavePlaces(new ArrayList<>(recommendedPlaces));

            // Return the cheap version of each place to save bandwidth
            // The place after a cheap search is already its cheap version but it might get
            // populated later with more data
            return new ApiResponse<LinkedHashSet<PlaceDTO>>(true, "Retrieved places", ConvertToDTOs(recommendedPlaces));
        } catch (Exception e) {
            return new ApiResponse<LinkedHashSet<PlaceDTO>>(false, e.toString(), null);
        }

    }

    private ApiResponse<AiClassificationResponse> ClassifyUserInput(String userInput)
            throws InterruptedException, JsonProcessingException {
        AiRequest<AiClassificationResponse> aiRequest = new AiRequest<>(userInput, "classifier",
                AiClassificationResponse.class);
        return aiManager.Call(AiProvider.OPENAI, aiRequest);
    }

    private Set<Place> FetchPlacesFromDB(Location coordinates, int radius, List<String> types, String intent) {
        // inclusive: cafe OR italian -> shows either or both
        // demanding: italian AND vegan -> shows only both
        return intent.equals("inclusive")
                ? new HashSet<>(
                        placeService.FindPlaceInclusive(coordinates.longitude(), coordinates.latitude(), radius, types))
                : new HashSet<>(placeService.FindPlacesDemanding(coordinates.longitude(), coordinates.latitude(),
                        radius, types));
    }

    // If too few entries in the db call the google maps api to retrieve
    // more(might still be less than the MAX num specified)
    private void FetchNewPlaces(Set<Place> recommendedPlaces, List<String> types, Location coordinates, int radius)
            throws JsonProcessingException, InterruptedException {
        int remaining = MAX_NEARBY_SEARCH_RESULTS - recommendedPlaces.size();
        List<Place> apiResults = nearSearchService.CheapNearbySearch(
                new NearbySearchRequest(new ObjectMapper().writeValueAsString(types), remaining, coordinates, radius));

        recommendedPlaces.addAll(apiResults);
    }

    private LinkedHashSet<PlaceDTO> ConvertToDTOs(Set<Place> places) {
        return places.stream()
                .map(Place::ToPlaceDTO)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
