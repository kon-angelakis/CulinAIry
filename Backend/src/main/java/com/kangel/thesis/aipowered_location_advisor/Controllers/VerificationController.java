package com.kangel.thesis.aipowered_location_advisor.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kangel.thesis.aipowered_location_advisor.Models.Records.ApiResponse;
import com.kangel.thesis.aipowered_location_advisor.Services.Authentication.Auth.VerificationService;

@RestController
@RequestMapping("/verify")
public class VerificationController {

    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @GetMapping
    public ResponseEntity<?> VerifyUser(@RequestParam String token) {
        ApiResponse<Void> response = verificationService.VerifyCode(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send")
    public ResponseEntity<?> SendCode(@RequestParam String email) {
        ApiResponse<Void> response = verificationService.SendCode(email);
        return ResponseEntity.ok(response);
    }

}
