package com.kangel.thesis.aipowered_location_advisor.Auth;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.kangel.thesis.aipowered_location_advisor.Users.User;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> LoginUser(@RequestBody Map<String, String> payload) {
        User loginedUser = authService.Login(payload.get("user"), payload.get("pass"));
        if (loginedUser != null)
            return new ResponseEntity<String>("User login successfully", HttpStatus.OK);
        else
            return new ResponseEntity<String>("User login failed", HttpStatus.UNAUTHORIZED);
    }

    // First step of registration sends OTP
    @PostMapping("/register")
    public ResponseEntity<String> RegisterUser(@RequestBody Map<String, String> payload) {
        try {
            User user = new User(payload.get("firstname"),
                    payload.get("lastname"),
                    payload.get("email"),
                    payload.get("username"),
                    payload.get("password"));

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
                    payload.get("password"));

            if (authService.VerifyAndRegister(user, payload.get("otp")))
                return new ResponseEntity<String>("User registered successfully", HttpStatus.CREATED);
            else
                return new ResponseEntity<String>("Invalid OTP verification", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<String>("User registration failed", HttpStatus.CONFLICT);
        }
    }

}