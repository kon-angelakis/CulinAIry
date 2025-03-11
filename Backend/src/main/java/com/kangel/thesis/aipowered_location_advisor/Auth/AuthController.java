package com.kangel.thesis.aipowered_location_advisor.Auth;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kangel.thesis.aipowered_location_advisor.Auth.OAuth2.OAuth2GoogleService;
import com.kangel.thesis.aipowered_location_advisor.Users.User;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private OAuth2GoogleService oauth2GoogleService;

    @PostMapping("/login")
    public ResponseEntity<?> LoginUser(@RequestBody Map<String, String> payload, HttpServletResponse response) {
        Map<String, Object> userLogined = authService.Login(payload.get("user"), payload.get("pass"));
        
        Map<String, Object> responseMap = Map.of(
                "User", payload.get("user"),
                "LoginStatus", userLogined.get("status"),
                "UserDetails", userLogined.get("details")
        );
        response.setHeader("authorization", "Bearer " + userLogined.get("token"));

        // Return the response in a json manner
        return new ResponseEntity<>(responseMap, (HttpStatus) userLogined.get("StatusCode"));
    }

    // First step of registration sends OTP
    @PostMapping("/register")
    public ResponseEntity<String> RegisterUser(@RequestBody Map<String, String> payload) {
        try {
            User user = new User(payload.get("firstname"),
                    payload.get("lastname"),
                    payload.get("email"),
                    payload.get("username"),
                    payload.get("password"),
                    "",
                    "LEGACY"); //Signed up with the legacy UP system

            if (authService.SendOTP(user))
                return new ResponseEntity<String>(String.format("Sent Verification code to: %s", user.getEmail()),
                        HttpStatus.OK);
            else
                return new ResponseEntity<String>("User already registered", HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<String>("Unexpected Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Used to validate front end fields
    @PostMapping("/register/validate")
    public ResponseEntity<Boolean> ValidateUser(@RequestBody Map<String, String> payload) {
        try {
            String user = payload.get("user");

            if (authService.UserExists(user) != null)
                return new ResponseEntity<Boolean>(true, HttpStatus.OK);
            return new ResponseEntity<Boolean>(false, HttpStatus.CONFLICT);

        } catch (Exception e) {
            return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Second step of registration verifies OTP and saves user
    @PostMapping("/verify")
    public ResponseEntity<String> VerifyUser(@RequestBody Map<String, String> payload) {
        try {
            User user = new User(payload.get("firstname"),
                    payload.get("lastname"),
                    payload.get("email"),
                    payload.get("username"),
                    payload.get("password"),
                    "",
                    "LEGACY"); //Signed up with the legacy UP system

            if (authService.VerifyAndRegister(user, payload.get("otp")))
                return new ResponseEntity<String>("User registered successfully", HttpStatus.CREATED);
            else
                return new ResponseEntity<String>("Invalid OTP verification", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<String>("User registration failed", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/authenticated")
    public ResponseEntity<String> AuthenticationCheck() {
        try{
            return new ResponseEntity<String>("User authorized", HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<String>("User needs to login first", HttpStatus.UNAUTHORIZED);
        }
    }


    @PostMapping("/oauth2/google")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> requestBody, HttpServletResponse response) throws IOException {
        String authCode = requestBody.get("code");

        // Get user id token from a call to the google api so as to extract user info
        String idToken = oauth2GoogleService.GetToken(authCode);

        // Decode the token for the data extraction
        String payload = new String(Base64.getUrlDecoder().decode(idToken.split("\\.")[1]));
        Map<String, String> userInfo = new ObjectMapper().readValue(payload, new TypeReference<>() {});
        User user = oauth2GoogleService.ExtractUser(userInfo);

        Map<String, Object> responseMap = oauth2GoogleService.Login(user, response);

        return new ResponseEntity<>(responseMap, (HttpStatusCode)responseMap.get("StatusCode"));
    }
    

}