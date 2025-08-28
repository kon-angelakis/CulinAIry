package com.kangel.thesis.aipowered_location_advisor.Controllers;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kangel.thesis.aipowered_location_advisor.Models.User;
import com.kangel.thesis.aipowered_location_advisor.Services.Authentication.OAuth2.OAuth2GoogleService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {

    private final OAuth2GoogleService oauth2GoogleService;

    public OAuth2Controller(OAuth2GoogleService oauth2GoogleService) {
        this.oauth2GoogleService = oauth2GoogleService;
    }

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> request, HttpServletResponse response)
            throws IOException {
        String authCode = request.get("code");

        // Get user id token from a call to the google api so as to extract user info
        String idToken = oauth2GoogleService.GetToken(authCode);

        // Decode the token for the data extraction
        String payload = new String(Base64.getUrlDecoder().decode(idToken.split("\\.")[1]));
        Map<String, String> userInfo = new ObjectMapper().readValue(payload, new TypeReference<>() {
        });
        User user = oauth2GoogleService.ExtractUser(userInfo);

        Map<String, Object> responseMap = oauth2GoogleService.Login(user, response);

        return new ResponseEntity<>(responseMap, (HttpStatusCode) responseMap.get("StatusCode"));
    }
}
