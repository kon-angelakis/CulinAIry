package com.kangel.thesis.aipowered_location_advisor.Services.Authentication.OAuth2;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.kangel.thesis.aipowered_location_advisor.Models.User;
import com.kangel.thesis.aipowered_location_advisor.Repositories.AuthRepo;
import com.kangel.thesis.aipowered_location_advisor.Services.Authentication.Auth.AuthService;
import com.kangel.thesis.aipowered_location_advisor.Services.Authentication.Auth.JwtService;
import com.kangel.thesis.aipowered_location_advisor.Services.Email.RegistrationEmailSender;

import jakarta.servlet.http.HttpServletResponse;

//Abstract class used for oauth2 services
@Component
public abstract class OAuth2Service {
    private final AuthRepo authRepo;
    private final AuthService authService;
    private final RegistrationEmailSender rEmailSender;
    private final JwtService jwtService;
    private final String name;

    public OAuth2Service(AuthRepo authRepo, AuthService authService, RegistrationEmailSender rEmailSender,
            JwtService jwtService, String name) {
        this.authRepo = authRepo;
        this.authService = authService;
        this.rEmailSender = rEmailSender;
        this.jwtService = jwtService;
        this.name = name;
    }

    // Gets access token from an api call to each service
    public abstract String GetToken(String code);

    // Extracts the user info from the token
    public abstract User ExtractUser(Map<String, String> userInfo);

    // Registers the user to the database
    public void Register(User user) throws IOException {
        try {
            authRepo.save(user);
            rEmailSender.SendEmail(user);
            System.out
                    .println(String.format("OAuth2: %s%nUser: %s%nRegistration: SUCCESS", this.name, user.getEmail()));
        } catch (Exception e) {
            System.out
                    .println(String.format("OAuth2: %s%nUser: %s%nRegistration: FAILURE", this.name, user.getEmail()));
        }
    }

    // Generates a login status if the authentication is successful also registers
    // the user if not in the db
    public Map<String, Object> Login(User user, HttpServletResponse response) throws IOException {
        if (authService.UserExists(user.getEmail()) == null)
            Register(user);

        Map<String, Object> responseMap;
        try {
            String jwtToken = jwtService.GenerateToken(authService.UserExists(user.getEmail()));
            response.setHeader("authorization", "Bearer " + jwtToken);
            responseMap = Map.of(
                    "UserDetails", jwtService.extractUser(jwtToken),
                    "StatusCode", HttpStatus.OK);
            return responseMap;
        } catch (Exception e) {
            responseMap = Map.of(
                    "UserDetails", "-",
                    "StatusCode", HttpStatus.UNAUTHORIZED);
            return responseMap;
        }
    }
}
