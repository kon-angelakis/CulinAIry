package com.kangel.thesis.aipowered_location_advisor.Services;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kangel.thesis.aipowered_location_advisor.Models.Place;
import com.kangel.thesis.aipowered_location_advisor.Models.Enums.AiProvider;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.AiClassificationResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.AiRequest;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ApiResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.GeolocationResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.Location;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.NearbySearchRequest;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.PaginatedResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.PaginationRequest;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.PlaceDTO;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.SearchRequest;

import jakarta.servlet.http.HttpServletRequest;
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

        public ApiResponse<PaginatedResponse<LinkedHashSet<PlaceDTO>>> SearchPlaces(@Valid SearchRequest request)
                        throws JsonProcessingException, InterruptedException {
                try {

                        AiClassificationResponse aiResponse = ClassifyUserInput(request.userInput()).data();
                        List<String> restaurantTypes = aiResponse.restaurantTypes();
                        String userIntent = aiResponse.userIntent();
                        Location coordinates = request.location();

                        List<Place> recommendedPlaces = FetchPlacesFromDB(coordinates, request.radius(),
                                        restaurantTypes,
                                        userIntent, request.pagingRequest(), request.sortField(),
                                        request.sortDirection());

                        // Check if we have enough results > 20
                        int totalElements = CountPlacesFromDB(coordinates, request.radius(),
                                        restaurantTypes,
                                        userIntent);

                        // Fetch additional places if less than MAX_NEARBY_SEARCH_RESULTS and check only
                        // once on the first page
                        if (totalElements < 12 && request.pagingRequest().page() == 0) {
                                FetchNewPlaces(restaurantTypes, coordinates,
                                                request.radius());
                                System.out.println("Fetched new places from API");
                        }

                        // After saving new places to the db refetch them, this makes pagination easier
                        // rather than adding the newly fetched results directly to recommendedPlaces
                        recommendedPlaces = FetchPlacesFromDB(coordinates, request.radius(), restaurantTypes,
                                        userIntent, request.pagingRequest(), request.sortField(),
                                        request.sortDirection());
                        PaginatedResponse<LinkedHashSet<PlaceDTO>> paginatedResponse = new PaginatedResponse<LinkedHashSet<PlaceDTO>>(
                                        recommendedPlaces.stream()
                                                        .map(Place::ToPlaceDTO)
                                                        .collect(Collectors.toCollection(LinkedHashSet::new)),
                                        totalElements,
                                        (int) Math.ceil((double) totalElements / request.pagingRequest().size()));
                        return new ApiResponse<PaginatedResponse<LinkedHashSet<PlaceDTO>>>(true,
                                        String.format("Showing %d places", recommendedPlaces.size()),
                                        paginatedResponse);
                } catch (Exception e) {
                        return new ApiResponse<PaginatedResponse<LinkedHashSet<PlaceDTO>>>(false, e.toString(), null);
                }

        }

        private ApiResponse<AiClassificationResponse> ClassifyUserInput(String userInput)
                        throws InterruptedException, JsonProcessingException {
                AiRequest<AiClassificationResponse> aiRequest = new AiRequest<>(userInput, "classifier",
                                AiClassificationResponse.class);
                return aiManager.Call(AiProvider.OPENAI, aiRequest);
        }

        private List<Place> FetchPlacesFromDB(Location coordinates, int radius, List<String> types, String intent,
                        PaginationRequest pagingRequest, String sortField, int sortDirection) {
                // inclusive: cafe OR italian -> shows either or both
                // demanding: italian AND vegan -> shows only both
                return intent.equals("inclusive")
                                ? placeService.FindPlaceInclusive(coordinates.longitude(),
                                                coordinates.latitude(), radius, types, pagingRequest, sortField,
                                                sortDirection)
                                : placeService.FindPlacesDemanding(coordinates.longitude(),
                                                coordinates.latitude(),
                                                radius, types, pagingRequest, sortField, sortDirection);
        }

        private int CountPlacesFromDB(Location coordinates, int radius, List<String> types, String intent) {
                return intent.equals("inclusive")
                                ? placeService.CountPlacesInclusive(coordinates.longitude(),
                                                coordinates.latitude(), radius, types)
                                : placeService.CountPlacesDemanding(coordinates.longitude(),
                                                coordinates.latitude(),
                                                radius, types);
        }

        // If too few entries in the db call the google maps api to retrieve
        // more(might still be less than the MAX num specified)
        private void FetchNewPlaces(List<String> types, Location coordinates, int radius)
                        throws JsonProcessingException, InterruptedException {
                List<Place> apiResults = nearSearchService.NearbySearch(
                                new NearbySearchRequest(new ObjectMapper().writeValueAsString(types), 20,
                                                coordinates, radius));
                placeService.SavePlaces(apiResults);
        }

        public ApiResponse<GeolocationResponse> RetrieveGeolocation(HttpServletRequest request) {
                String ip = request.getHeader("X-Forwarded-For");
                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                        ip = request.getRemoteAddr();
                } else { // on proxies take the first
                        ip = ip.split(",")[0];
                }
                WebClient webClient = WebClient.create();
                // Using ip-api.com for IP geolocation
                String url = "http://ip-api.com/json/" + ip + "?fields=status,city,lat,lon,query";
                GeolocationResponse response = webClient.get()
                                .uri(url)
                                .retrieve()
                                .bodyToMono(GeolocationResponse.class).block();
                if (response != null && !Boolean.parseBoolean(response.status())) {
                        return new ApiResponse<GeolocationResponse>(true, "Geolocation retrieved successfully",
                                        response);
                } else {
                        return new ApiResponse<GeolocationResponse>(false, "Failed to retrieve geolocation", null);
                }
        }

}
