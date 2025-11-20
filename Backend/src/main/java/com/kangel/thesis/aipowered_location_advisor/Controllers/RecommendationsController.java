package com.kangel.thesis.aipowered_location_advisor.Controllers;

import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kangel.thesis.aipowered_location_advisor.Models.Records.ApiResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.Location;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.PlaceDTO;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.SimilarPlacesRequest;
import com.kangel.thesis.aipowered_location_advisor.Services.RecommendationsService;

@RestController
@RequestMapping("/recommendations")
public class RecommendationsController {
    private final RecommendationsService rService;

    public RecommendationsController(RecommendationsService rService) {
        this.rService = rService;
    }

    @PostMapping("/best")
    public ResponseEntity<?> BestPlaces(@RequestBody Location request) {
        ApiResponse<LinkedHashSet<PlaceDTO>> response = rService.FindBestPlaces(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/trending")
    public ResponseEntity<?> TrendingPlaces(@RequestBody Location request) {
        ApiResponse<LinkedHashSet<PlaceDTO>> response = rService.FindTrendingPlaces(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/similar")
    public ResponseEntity<?> SimilarPlaces(@RequestBody SimilarPlacesRequest request) {
        ApiResponse<LinkedHashSet<PlaceDTO>> response = rService.FindSimilarPlaces(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/curated")
    public ResponseEntity<?> CuratedPlaces(@RequestBody Location request) {
        ApiResponse<List<PlaceDTO>> response = rService.FindCuratedPlaces(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/revisit")
    public ResponseEntity<?> RevisitPastPlaces(@RequestBody Location request) {
        ApiResponse<List<PlaceDTO>> response = rService.FindPastPlaces(request);

        return ResponseEntity.ok(response);
    }

}
