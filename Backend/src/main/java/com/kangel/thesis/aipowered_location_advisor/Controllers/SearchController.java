package com.kangel.thesis.aipowered_location_advisor.Controllers;

import java.util.LinkedHashSet;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ApiResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.GeolocationResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.PaginatedResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.PlaceDTO;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.SearchRequest;
import com.kangel.thesis.aipowered_location_advisor.Services.SearchService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/places")
    public ResponseEntity<?> CheapSearch(
            @Valid @RequestBody SearchRequest request) throws JsonProcessingException, InterruptedException {
        ApiResponse<PaginatedResponse<LinkedHashSet<PlaceDTO>>> response = searchService.SearchPlaces(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/geolocation") // For general geolocation before and if the user provides accurate location
                                // data
    public ResponseEntity<?> GeoSearch(HttpServletRequest request) {
        ApiResponse<GeolocationResponse> response = searchService.RetrieveGeolocation(request);
        return ResponseEntity.ok(response);
    }

}
