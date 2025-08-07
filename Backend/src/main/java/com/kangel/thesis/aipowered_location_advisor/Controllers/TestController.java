package com.kangel.thesis.aipowered_location_advisor.Controllers;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kangel.thesis.aipowered_location_advisor.Models.AIAgent;
import com.kangel.thesis.aipowered_location_advisor.Models.Place;
import com.kangel.thesis.aipowered_location_advisor.Models.Review;
import com.kangel.thesis.aipowered_location_advisor.Repositories.PlacesRepo;
import com.kangel.thesis.aipowered_location_advisor.Services.AIService;
import com.kangel.thesis.aipowered_location_advisor.Services.GPlacesService;
import com.kangel.thesis.aipowered_location_advisor.Services.RequestValidator;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/v1/api/test")
public class TestController {
    
    @Autowired
    private RequestValidator requestValidator;
    @Autowired
    private AIService aiService;
    @Autowired
    private GPlacesService gPlacesService;
    @Autowired
    private PlacesRepo pRepo;
    
    @PostMapping("/location")
    public String postMethodName(@RequestBody Map<String,Object> payload) {
        requestValidator.validateRequiredKeys(payload, "location", "radius");
        return "OK!";
    }

    @PostMapping("/search")
    public ResponseEntity<Map<String, Object>> PlaceSearch(@RequestBody Map<String,Object> payload) throws JsonProcessingException, InterruptedException {
        requestValidator.validateRequiredKeys(payload, "user_input", "location", "radius");
        //AiAgent figures out which type of restaurant types the user might be interested in
        AIAgent queryClassifier = new AIAgent("asst_cfRdFvTm15gzE1z6pQcsT5Tx", payload.get("user_input").toString(), aiService);
        Map<String, Object> agentResponse = queryClassifier.Run();
        List<String> restTypes = (List<String>) agentResponse.get("types");
        payload.put("restaurant_types", new ObjectMapper().writeValueAsString(restTypes));
        Map<String, Object> nearbySearchResults = gPlacesService.NearbySearch(payload);
        // Save a Place object in the database for every response place object
        List<Map<String, Object>> places = (List<Map<String, Object>>) nearbySearchResults.get("places");
        for (Map<String, Object> place : places) {
            Place tmp = gPlacesService.ExtractPlaceData(place);
            pRepo.save(tmp);
        }     

        return(new ResponseEntity<>(null, HttpStatus.OK));
    }
    
    
    
}
