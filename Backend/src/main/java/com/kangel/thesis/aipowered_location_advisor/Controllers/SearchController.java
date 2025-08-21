package com.kangel.thesis.aipowered_location_advisor.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kangel.thesis.aipowered_location_advisor.Models.Place;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.SearchRequest;
import com.kangel.thesis.aipowered_location_advisor.Services.SearchService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping
    public ResponseEntity<?> PlaceSearch(
            @Valid @RequestBody SearchRequest request) throws JsonProcessingException, InterruptedException {
        List<Place> results = searchService.SearchPlaces(request);
        return (new ResponseEntity<>(results, HttpStatus.OK));
    }
}
