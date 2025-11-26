package com.kangel.thesis.aipowered_location_advisor.Services.Authentication.OAuth2;

import java.util.Base64;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kangel.thesis.aipowered_location_advisor.Config.Security.Auth.HashingConfig;
import com.kangel.thesis.aipowered_location_advisor.Models.User;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ApiResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.GoogleUser;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.LoginResponse;
import com.kangel.thesis.aipowered_location_advisor.Services.UserService;
import com.kangel.thesis.aipowered_location_advisor.Services.Authentication.Auth.JwtService;
import com.kangel.thesis.aipowered_location_advisor.Services.Messaging.Email.EmailFactory;
import com.kangel.thesis.aipowered_location_advisor.Services.Messaging.Email.SpringEmailService;

import io.github.cdimascio.dotenv.Dotenv;

@Component
public class OAuth2GoogleService extends OAuth2Service {

    private final HashingConfig hashingConfig;
    private final Dotenv env;

    public OAuth2GoogleService(UserService userService, SpringEmailService emailService,
            EmailFactory emailFactory,
            JwtService jwtService, HashingConfig hashingConfig, Dotenv env) {
        super(userService, emailService, emailFactory, jwtService);
        this.hashingConfig = hashingConfig;
        this.env = env;
    }

    @Override
    public ApiResponse<LoginResponse> CreateOauth2User(String code)
            throws JsonMappingException, JsonProcessingException {
        // Get user id token from a call to the google api so as to extract user info
        String idToken = GetToken(code);

        // Decode the token for the data extraction
        String payload = new String(Base64.getUrlDecoder().decode(idToken.split("\\.")[1]));
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        GoogleUser googleUser = mapper.readValue(payload, GoogleUser.class);
        User user = ExtractUser(googleUser);

        // Login directly if the user doesnt exist else register first
        ApiResponse<LoginResponse> response = Login(user);
        return response;
    }

    @Override
    protected String GetToken(String code) {
        WebClient webClient = WebClient.create("https://oauth2.googleapis.com");
        Map<String, String> tokenResponse = webClient.post()
                .uri("/token")
                .bodyValue(Map.of(
                        "code", code,
                        "client_id", env.get("GOOGLE_CLIENT_ID"),
                        "client_secret", env.get("GOOGLE_CLIENT_SECRET"),
                        "redirect_uri", "https://qyvmzivriyxf.share.zrok.io",
                        "grant_type", "authorization_code"))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {
                })
                .block();
        return tokenResponse.get("id_token");
    }

    private User ExtractUser(GoogleUser googleUser) {
        User user = new User(
                googleUser.given_name(),
                googleUser.family_name(),
                googleUser.email(),
                hashingConfig.GenerateRandomUsername(8),
                googleUser.picture(),
                "GOOGLE");
        user.setVerified(true);
        return user;
    }

}
