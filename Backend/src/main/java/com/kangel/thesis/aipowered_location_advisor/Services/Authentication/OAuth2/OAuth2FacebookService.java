package com.kangel.thesis.aipowered_location_advisor.Services.Authentication.OAuth2;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.kangel.thesis.aipowered_location_advisor.Models.User;
import com.kangel.thesis.aipowered_location_advisor.Repositories.AuthRepo;
import com.kangel.thesis.aipowered_location_advisor.Services.Authentication.Auth.AuthService;
import com.kangel.thesis.aipowered_location_advisor.Services.Authentication.Auth.JwtService;
import com.kangel.thesis.aipowered_location_advisor.Services.Email.RegistrationEmailSender;

@Component
public class OAuth2FacebookService extends OAuth2Service{

    public OAuth2FacebookService(AuthRepo authRepo, AuthService authService, RegistrationEmailSender rEmailSender,
            JwtService jwtService) {
        super(authRepo, authService, rEmailSender, jwtService, "FACEBOOK");
        //TODO Auto-generated constructor stub
    }

    @Override
    public String GetToken(String code) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'GetToken'");
    }

    @Override
    public User ExtractUser(Map<String, String> userInfo) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'ExtractUser'");
    }

}
