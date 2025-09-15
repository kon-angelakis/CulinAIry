package com.kangel.thesis.aipowered_location_advisor.Services.Authentication.Auth;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kangel.thesis.aipowered_location_advisor.Models.User;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ApiResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.LoginRequest;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.LoginResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.RegisterRequest;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ValidationRequest;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ValidationResponse;
import com.kangel.thesis.aipowered_location_advisor.Services.UserService;

@Service
public class AuthService {

    private final UserService userService;
    private final VerificationService verificationService;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthService(UserService userService, BCryptPasswordEncoder encoder, AuthenticationManager authManager,
            JwtService jwtService, VerificationService verificationService) {
        this.userService = userService;
        this.verificationService = verificationService;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    public ApiResponse<Void> Register(RegisterRequest request) {
        User user = Optional.ofNullable(userService.GetUser(request.username()))
                .orElse(userService.GetUser(request.email()));
        if (user == null) {
            user = new User(
                    request.firstName(),
                    request.lastName(),
                    request.email(),
                    request.username(),
                    encoder.encode(request.password()),
                    request.pfp(),
                    "LEGACY");
            user.setVerified(false);
            user.setVerificationCode(jwtService.GenerateToken(user));
            user = userService.SaveUser(user);
            verificationService.SendCode(user.getEmail());
            return new ApiResponse<>(true, String.format("Sent verification code to %s", user.getEmail()), null);
        }
        // user exists or token not expired
        if (user.isVerified() || !jwtService.validateToken(user.getVerificationCode())) {
            String msg = user.isVerified() ? "User already exists"
                    : "User already registered and awaiting verification";
            return new ApiResponse<>(false, msg, null);
        }

        // expired token?
        user.setVerificationCode(jwtService.GenerateToken(user));
        user = userService.SaveUser(user);
        verificationService.SendCode(user.getEmail());
        return new ApiResponse<>(true, String.format("Sent verification code to %s", user.getEmail()), null);
    }

    public ApiResponse<LoginResponse> Login(LoginRequest request) {
        User tmpAuthUser = userService.GetUser(request.user());
        if (tmpAuthUser == null || !tmpAuthUser.isVerified())
            return new ApiResponse<>(false, "User not found or is not verified", null);
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(request.user(), request.pass()));
            String jwt = jwtService.GenerateToken(tmpAuthUser);
            return new ApiResponse<>(true, "User logined",
                    new LoginResponse(tmpAuthUser.ToUserDTO(), jwt));
        } catch (AuthenticationException e) {
            return new ApiResponse<>(false, "Couldn't authenticate user", null);
        }
    }

    public ApiResponse<ValidationResponse> ValidateFields(ValidationRequest request) {
        boolean eitherExist = userService.UserExists(request.email()) || userService.UserExists(request.username());
        return new ApiResponse<>(eitherExist, "Validation", new ValidationResponse(
                userService.UserExists(request.email()), userService.UserExists(request.username())));
    }

}
