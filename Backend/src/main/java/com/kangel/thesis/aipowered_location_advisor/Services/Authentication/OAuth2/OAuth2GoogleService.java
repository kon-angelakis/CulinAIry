package com.kangel.thesis.aipowered_location_advisor.Services.Authentication.OAuth2;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.kangel.thesis.aipowered_location_advisor.Config.Security.Auth.HashingConfig;
import com.kangel.thesis.aipowered_location_advisor.Models.User;
import com.kangel.thesis.aipowered_location_advisor.Repositories.AuthRepo;
import com.kangel.thesis.aipowered_location_advisor.Services.Authentication.Auth.AuthService;
import com.kangel.thesis.aipowered_location_advisor.Services.Authentication.Auth.JwtService;
import com.kangel.thesis.aipowered_location_advisor.Services.Email.RegistrationEmailSender;

import io.github.cdimascio.dotenv.Dotenv;

@Component
public class OAuth2GoogleService extends OAuth2Service{

    private final HashingConfig hashingConfig;
    private final Dotenv env;

    @Autowired
    public OAuth2GoogleService(AuthRepo authRepo, AuthService authService, RegistrationEmailSender rEmailSender, JwtService jwtService, HashingConfig hashingConfig, Dotenv env){
        super(authRepo, authService, rEmailSender, jwtService, "GOOGLE");
        this.hashingConfig = hashingConfig;
        this.env = env;
    }

    @Override
    public String GetToken(String code) {
       WebClient webClient = WebClient.create("https://oauth2.googleapis.com");
        Map<String, String> tokenResponse = webClient.post()
            .uri("/token")
            .bodyValue(Map.of(
                "code", code,
                "client_id", env.get("GOOGLE_CLIENT_ID"),
                "client_secret", env.get("GOOGLE_CLIENT_SECRET"),
                "redirect_uri", "http://localhost:5173",
                "grant_type", "authorization_code"
            ))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
            .block();
        return tokenResponse.get("id_token");
    }

    @Override
    public User ExtractUser(Map<String, String> userInfo) {
        User user = new User(
            userInfo.get("given_name"),
            userInfo.get("family_name"),
            userInfo.get("email"),
            hashingConfig.GenerateRandomUsername(8),
            userInfo.get("picture"),
            "GOOGLE"
        );
        return user;
    }

}
