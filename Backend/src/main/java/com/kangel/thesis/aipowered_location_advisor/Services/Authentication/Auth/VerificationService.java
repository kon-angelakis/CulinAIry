package com.kangel.thesis.aipowered_location_advisor.Services.Authentication.Auth;

import java.util.Map;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.kangel.thesis.aipowered_location_advisor.Models.User;
import com.kangel.thesis.aipowered_location_advisor.Models.Enums.EmailTemplate;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ApiResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.UserDTO;
import com.kangel.thesis.aipowered_location_advisor.Services.UserService;
import com.kangel.thesis.aipowered_location_advisor.Services.Messaging.Email.EmailFactory;
import com.kangel.thesis.aipowered_location_advisor.Services.Messaging.Email.SpringEmailService;

@Service
public class VerificationService {

    private final EmailFactory emailFactory;
    private final SpringEmailService emailService;
    private final UserService userService;
    private final JwtService jwtService;

    public VerificationService(EmailFactory emailFactory, SpringEmailService emailService,
            UserService userService, JwtService jwtService) {
        this.emailFactory = emailFactory;
        this.emailService = emailService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    public ApiResponse<Void> VerifyCode(String token) {
        UserDTO userdto = jwtService.extractUser(token);
        User user = userService.GetUser(userdto.email());
        if (user == null)
            return new ApiResponse<>(false, "No user found", null);
        if (user.isVerified())
            return new ApiResponse<>(false, "User already verified", null);

        if (user.getVerificationCode() != null) {
            if (!user.getVerificationCode().equals(token)) {
                return new ApiResponse<>(false, "Link expired register again", null);
            }
        }
        user.setVerified(true);
        user.setVerificationCode(null);
        userService.SaveUser(user);
        emailService.SendMail(
                emailFactory.Create(EmailTemplate.THANKYOU, user.getEmail(), Map.of("name", user.getFirstName())));
        return new ApiResponse<>(true, "User verified successfully", null);
    }

    @Async
    public ApiResponse<Void> SendCode(String email) {
        User user = userService.GetUser(email);
        if (user == null)
            return new ApiResponse<>(false, "No user found", null);
        if (user.isVerified())
            return new ApiResponse<>(false, "User already verified", null);
        String verificationLink = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/verify")
                .queryParam("token", user.getVerificationCode())
                .build()
                .toUriString();
        emailService.SendMail(emailFactory.Create(EmailTemplate.VERIFY, user.getEmail(),
                Map.of("name", user.getFirstName(), "link", verificationLink)));
        return new ApiResponse<>(true, String.format("Verification code sent to %s", user.getEmail()), null);
    }
}
