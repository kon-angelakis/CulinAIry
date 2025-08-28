package com.kangel.thesis.aipowered_location_advisor.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kangel.thesis.aipowered_location_advisor.Models.Records.ApiResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.LoginRequest;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.LoginResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.RegisterRequest;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ValidationRequest;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ValidationResponse;
import com.kangel.thesis.aipowered_location_advisor.Services.Authentication.Auth.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> Register(@RequestBody @Valid RegisterRequest request) {
        ApiResponse<Void> response = authService.Register(request);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/login")
    public ResponseEntity<?> Login(@RequestBody @Valid LoginRequest request) {
        ApiResponse<LoginResponse> response = authService.Login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    public ResponseEntity<?> Validate(@RequestBody ValidationRequest request) { // Checks if registration fields already
                                                                                // exist
        ApiResponse<ValidationResponse> response = authService.ValidateFields(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/authenticated")
    public ResponseEntity<String> AuthenticationCheck() { // Used for protected routing inside the frontend
        try {
            return new ResponseEntity<String>("User authorized", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("User needs to login first", HttpStatus.UNAUTHORIZED);
        }
    }

}
