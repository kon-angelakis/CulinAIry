package com.kangel.thesis.aipowered_location_advisor.Services.Authentication.OAuth2;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.kangel.thesis.aipowered_location_advisor.Models.User;
import com.kangel.thesis.aipowered_location_advisor.Models.Enums.EmailTemplate;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ApiResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.LoginResponse;
import com.kangel.thesis.aipowered_location_advisor.Services.UserService;
import com.kangel.thesis.aipowered_location_advisor.Services.Authentication.Auth.JwtService;
import com.kangel.thesis.aipowered_location_advisor.Services.Messaging.Email.EmailFactory;
import com.kangel.thesis.aipowered_location_advisor.Services.Messaging.Email.SpringEmailService;

//Abstract class used for oauth2 services
@Component
public abstract class OAuth2Service {
    private final UserService userService;
    private final SpringEmailService emailService;
    private final EmailFactory emailFactory;
    private final JwtService jwtService;

    public <RegistrationEmailSender> OAuth2Service(UserService userService, SpringEmailService emailService,
            EmailFactory emailFactory,
            JwtService jwtService) {
        this.userService = userService;
        this.emailService = emailService;
        this.emailFactory = emailFactory;
        this.jwtService = jwtService;
    }

    // Main exeution
    public abstract ApiResponse<LoginResponse> CreateOauth2User(String code)
            throws JsonMappingException, JsonProcessingException;

    // Gets access token from an api call to each service
    protected abstract String GetToken(String code);

    // Registers the user to the database
    protected User Register(User user) {
        try {
            user = userService.SaveUser(user);
            emailService.SendMail(
                    emailFactory.Create(EmailTemplate.THANKYOU, user.getEmail(), Map.of("name", user.getFirstName())));
            return (user);
        } catch (Exception e) {
            throw (e);
        }
    }

    // Generates a login status if the authentication is successful also registers
    // the user if not in the db
    protected ApiResponse<LoginResponse> Login(User user) {
        if (!userService.UserExists(user.getEmail()))
            user = Register(user);
        if (userService.UserExists(user.getEmail())
                && userService.GetUser(user.getEmail()).getRegistration_method().equals("LEGACY"))
            return new ApiResponse<LoginResponse>(false, "An account is already bound to that email address", null);

        try {
            user = userService.GetUser(user.getEmail());
            String jwt = jwtService.GenerateToken(user);
            return new ApiResponse<LoginResponse>(true, "User logined",
                    new LoginResponse(user.ToUserDTO(), jwt));
        } catch (AuthenticationException e) {
            return new ApiResponse<LoginResponse>(false, "Couldn't authenticate user", null);
        }
    }

}
