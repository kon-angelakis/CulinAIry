package com.kangel.thesis.aipowered_location_advisor.Services.Authentication.OAuth2;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.kangel.thesis.aipowered_location_advisor.Models.User;
import com.kangel.thesis.aipowered_location_advisor.Models.Enums.EmailTemplate;
import com.kangel.thesis.aipowered_location_advisor.Services.UserService;
import com.kangel.thesis.aipowered_location_advisor.Services.Authentication.Auth.JwtService;
import com.kangel.thesis.aipowered_location_advisor.Services.Messaging.Email.EmailFactory;
import com.kangel.thesis.aipowered_location_advisor.Services.Messaging.Email.SpringEmailService;

import jakarta.servlet.http.HttpServletResponse;

//Abstract class used for oauth2 services
@Component
public abstract class OAuth2Service {
    private final UserService userService;
    private final SpringEmailService emailService;
    private final EmailFactory emailFactory;
    private final JwtService jwtService;
    private final String name;

    public <RegistrationEmailSender> OAuth2Service(UserService userService, SpringEmailService emailService,
            EmailFactory emailFactory,
            JwtService jwtService, String name) {
        this.userService = userService;
        this.emailService = emailService;
        this.emailFactory = emailFactory;
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
            userService.SaveUser(user);
            emailService.SendMail(
                    emailFactory.Create(EmailTemplate.THANKYOU, user.getEmail(), Map.of("name", user.getFirstName())));
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
        if (userService.GetUser(user.getEmail()) == null)
            Register(user);

        Map<String, Object> responseMap;
        try {
            String jwtToken = jwtService.GenerateToken(userService.GetUser(user.getEmail()));
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
