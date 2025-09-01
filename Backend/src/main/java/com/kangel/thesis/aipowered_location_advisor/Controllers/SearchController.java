package com.kangel.thesis.aipowered_location_advisor.Controllers;

import java.util.LinkedHashSet;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ApiResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.PlaceDTO;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.SearchRequest;
import com.kangel.thesis.aipowered_location_advisor.Services.SearchService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping
    public ResponseEntity<?> PlaceSearch(
            @Valid @RequestBody SearchRequest request) throws JsonProcessingException, InterruptedException {
        ApiResponse<LinkedHashSet<PlaceDTO>> response = searchService.SearchPlaces(request);
        return (new ResponseEntity<>(response, HttpStatus.OK));
    }
}
