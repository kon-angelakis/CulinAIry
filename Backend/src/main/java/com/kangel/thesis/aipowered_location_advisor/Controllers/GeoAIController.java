package com.kangel.thesis.aipowered_location_advisor.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kangel.thesis.aipowered_location_advisor.Models.AIAgent;
import com.kangel.thesis.aipowered_location_advisor.Services.GPlacesService;
import com.kangel.thesis.aipowered_location_advisor.Services.RequestValidator;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/v1/api/geolocation")
public class GeoAIController {

    @Autowired
    private RequestValidator requestValidator;
    @Autowired
    private GPlacesService gPlacesService;
    
    @PostMapping("/search")
    public ResponseEntity<String> PlaceSearch(@RequestBody Map<String,Object> payload) throws JsonProcessingException, InterruptedException {
        
        return new ResponseEntity<String>("OK", HttpStatus.OK);
    }
    
}
