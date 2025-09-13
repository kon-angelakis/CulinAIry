package com.kangel.thesis.aipowered_location_advisor.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ApiResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.LoginResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.OAuth2LoginRequest;
import com.kangel.thesis.aipowered_location_advisor.Services.Authentication.OAuth2.OAuth2GoogleService;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

    private final OAuth2GoogleService oauth2GoogleService;

    public OAuth2Controller(OAuth2GoogleService oauth2GoogleService) {
        this.oauth2GoogleService = oauth2GoogleService;
    }

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody OAuth2LoginRequest request)
            throws JsonMappingException, JsonProcessingException {
        ApiResponse<LoginResponse> response = oauth2GoogleService.CreateOauth2User(request.code());
        return ResponseEntity.ok(response);
    }
}
