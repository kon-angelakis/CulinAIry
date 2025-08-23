package com.kangel.thesis.aipowered_location_advisor.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kangel.thesis.aipowered_location_advisor.Models.AIAgent;
import com.kangel.thesis.aipowered_location_advisor.Models.Place;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.Location;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.NearbySearchRequest;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.SearchRequest;
import com.kangel.thesis.aipowered_location_advisor.Repositories.PlacesRepo;

import jakarta.validation.Valid;

@Service
public class SearchService {

    private final AIService aiService;
    private final NearbySearchService nearSearchService;
    private final PlacesRepo placesRepo;

    public SearchService(AIService aiService, NearbySearchService nearSearchService, PlacesRepo placesRepo) {
        this.aiService = aiService;
        this.nearSearchService = nearSearchService;
        this.placesRepo = placesRepo;
    }

    public List<Place> SearchPlaces(@Valid SearchRequest request) throws JsonProcessingException, InterruptedException {
        // AiAgent figures out which type of restaurant types the user might be
        // interested in
        // It also figures out the intent being demanding (eg a restaurant thats both
        // italian and vegan) or inclusive (eg a cafe or bar or both)
        AIAgent queryClassifier = new AIAgent("asst_cfRdFvTm15gzE1z6pQcsT5Tx", request.user_input(), aiService);
        Map<String, Object> agentResponse = queryClassifier.Run();
        List<String> restTypes = (List<String>) agentResponse.get("types");
        String userIntent = (String) agentResponse.get("intent");

        Location coordinates = request.location();

        List<Place> recommendedPlaces = new ArrayList<>();

        if (userIntent.equals("inclusive"))
            recommendedPlaces = placesRepo.findNearbyPlacesInclusive(coordinates.longitude(), coordinates.latitude(),
                    request.radius(), restTypes);
        else // demanding intent
            recommendedPlaces = placesRepo.findNearbyPlacesDemanding(coordinates.longitude(), coordinates.latitude(),
                    request.radius(), restTypes);

        Map<String, Place> recommendedPlacesMap = recommendedPlaces.size() != 0 ? recommendedPlaces.stream()
                .collect(Collectors.toMap(Place::getId, Function.identity())) : new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        // Update existing entries if they havent been updated in more than 2 weeks
        for (Place place : recommendedPlaces) {
            if (place.getDateUpdated().isBefore(now.minusWeeks(2))) {
                System.out.println(String.format("Place with id %s time updated", place.getId()));
                placesRepo.save(nearSearchService.SearchPlaceDetails(place.getId()));
            }
        }
        // Check arraylist size if < 20 then do a gmap api call
        if (recommendedPlaces.size() < 20) {
            int numofPlaces = 20 - recommendedPlaces.size();
            List<Place> nearbySearchResults = nearSearchService.NearbySearch(new NearbySearchRequest(
                    new ObjectMapper().writeValueAsString(restTypes), numofPlaces, coordinates, request.radius()));
            // Assuming the recommendedPlacesMap has every result matching the user query
            // (<20)
            // If any id of the nearbySearchResults places is already in the map don't save
            // it in the db else save it
            for (Place place : nearbySearchResults) {
                if (!recommendedPlacesMap.containsKey(place.getId())) {
                    recommendedPlacesMap.put(place.getId(), place);
                    placesRepo.save(place);
                }
            }
            recommendedPlaces = new ArrayList<>(recommendedPlacesMap.values());
        }
        return recommendedPlaces;
    }
}
