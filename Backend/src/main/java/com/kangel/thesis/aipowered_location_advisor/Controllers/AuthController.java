package com.kangel.thesis.aipowered_location_advisor.Controllers;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kangel.thesis.aipowered_location_advisor.Models.User;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.AuthUserRequest;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.RegisterUserRequest;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.UserDTO;
import com.kangel.thesis.aipowered_location_advisor.Services.Authentication.Auth.AuthService;
import com.kangel.thesis.aipowered_location_advisor.Services.Authentication.OAuth2.OAuth2GoogleService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final OAuth2GoogleService oauth2GoogleService;

    public AuthController(AuthService authService, OAuth2GoogleService oauth2GoogleService) {
        this.authService = authService;
        this.oauth2GoogleService = oauth2GoogleService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> LoginUser(@RequestBody @Valid AuthUserRequest request, HttpServletResponse response) {
        Map<String, Object> userLogined = authService.Login(
                request.user(),
                request.pass());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userLogined.get("token"))
                .body(userLogined.get("details"));
    }

    // First step of registration sends OTP
    @PostMapping("/register")
    public ResponseEntity<String> RegisterUser(@RequestBody @Valid UserDTO userRequest) {
        try {
            if (authService.SendOTP(userRequest.username(), userRequest.email()))
                return new ResponseEntity<String>(String.format("Sent Verification code to: %s", userRequest.email()),
                        HttpStatus.OK);
            else
                return new ResponseEntity<String>("User already registered", HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<String>("Unexpected Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Used to validate front end email and username fields (show the user if fields
    // already exist in db)
    @PostMapping("/register/validate")
    public ResponseEntity<Boolean> ValidateUser(@RequestBody @Valid AuthUserRequest request) {
        try {
            String user = request.user(); // could be user or email

            if (authService.UserExists(user) != null)
                return new ResponseEntity<Boolean>(true, HttpStatus.OK);
            return new ResponseEntity<Boolean>(false, HttpStatus.CONFLICT);

        } catch (Exception e) {
            return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Second step of registration verifies OTP and saves user
    @PostMapping("/verify")
    public ResponseEntity<String> VerifyUser(@RequestBody @Valid RegisterUserRequest request) {
        try {
            User user = new User(request.user().firstName(),
                    request.user().lastName(),
                    request.user().email(),
                    request.user().username(),
                    request.user().password(),
                    request.user().pfp(),
                    "LEGACY"); // Signed up with the legacy UP system

            if (authService.VerifyAndRegister(user, request.otp()))
                return new ResponseEntity<String>("User registered successfully", HttpStatus.CREATED);
            else
                return new ResponseEntity<String>("Invalid OTP verification", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<String>("User registration failed", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/authenticated")
    public ResponseEntity<String> AuthenticationCheck() {
        try {
            return new ResponseEntity<String>("User authorized", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("User needs to login first", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/oauth2/google")
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