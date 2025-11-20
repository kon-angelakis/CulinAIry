package com.kangel.thesis.aipowered_location_advisor.Controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kangel.thesis.aipowered_location_advisor.Models.Place;
import com.kangel.thesis.aipowered_location_advisor.Models.Review;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ApiResponse;
import com.kangel.thesis.aipowered_location_advisor.Services.PlaceService;

@RestController
@RequestMapping("/places/{placeId}")
public class PlaceController {

    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping("/details")
    public ResponseEntity<?> PlaceDetails(@PathVariable String placeId) {
        ApiResponse<Place> response = placeService.FindPlace(placeId);
        return ResponseEntity.ok(response);
    }

    // Updates place object with reviews doesnt return review objects
    @GetMapping("/reviews")
    public ResponseEntity<?> PlaceReviews(@PathVariable String placeId) {
        ApiResponse<List<Review>> response = placeService.FindReviews(placeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/favouritecount")
    public ResponseEntity<?> PlaceFavouriteCount(@PathVariable String placeId) {
        ApiResponse<Integer> response = placeService.FindTimesFavourited(placeId);
        return ResponseEntity.ok(response);
    }

}
