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
            List<Place> placesToUpdate = UpdateStalePlaces(recommendedPlaces);

            // Fetch additional places if less than 20
            if (recommendedPlaces.size() < 20) {
                FetchAdditionalPlaces(recommendedPlaces, placesToUpdate, restaurantTypes, coordinates,
                        request.radius());
            }

            // Save all updated/new places
            placeService.SavePlaces(placesToUpdate);
            // Dont return the whole place object to the frontend just the DTO, to get the
            // whole object info the user must call the PlaceController
            return new ApiResponse<LinkedHashSet<PlaceDTO>>(true, "Retrieved places", ConvertToDTOs(recommendedPlaces));
        } catch (Exception e) {
            return new ApiResponse<LinkedHashSet<PlaceDTO>>(false, "Could not retrieve places", null);
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

    // Each 2 weeks update the stale place data
    private List<Place> UpdateStalePlaces(Set<Place> places) throws InterruptedException {
        List<Place> updatedPlaces = new ArrayList<>();
        LocalDateTime twoWeeksAgo = LocalDateTime.now().minusWeeks(2);

        List<Place> stalePlaces = places.stream()
                .filter(p -> p.getDateUpdated().isBefore(twoWeeksAgo))
                .toList();

        for (Place stale : stalePlaces) {
            places.remove(stale);
            Place updated = nearSearchService.SearchPlaceDetails(stale.getId());
            places.add(updated);
            updatedPlaces.add(updated);
        }

        return updatedPlaces;
    }

    // If too few entries in the db < 20 call the google maps api to retrieve
    // more(might still be <20)
    private void FetchAdditionalPlaces(Set<Place> recommendedPlaces, List<Place> placesToUpdate,
            List<String> types, Location coordinates, int radius) throws JsonProcessingException, InterruptedException {
        int remaining = 20 - recommendedPlaces.size();
        List<Place> apiResults = nearSearchService.NearbySearch(
                new NearbySearchRequest(new ObjectMapper().writeValueAsString(types), remaining, coordinates, radius));

        apiResults.stream()
                .filter(recommendedPlaces::add)
                .forEach(placesToUpdate::add);
    }

    private LinkedHashSet<PlaceDTO> ConvertToDTOs(Set<Place> places) {
        return places.stream()
                .map(Place::ToPlaceDTO)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
