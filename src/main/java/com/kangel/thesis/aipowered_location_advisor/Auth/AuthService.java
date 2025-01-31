package com.kangel.thesis.aipowered_location_advisor.Auth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kangel.thesis.aipowered_location_advisor.Services.EmailService;
import com.kangel.thesis.aipowered_location_advisor.Services.OTPService;
import com.kangel.thesis.aipowered_location_advisor.Users.User;

@Service
public class AuthService {
    @Autowired
    private AuthRepo authRepo;

    public User Login(String user, String pass) {
        // Check for login with email or username
        if ((user.toString().contains("@"))) {
            return authRepo.findByEmailAndPassword(user, pass).orElse(null);
        } else {
            return authRepo.findByUsernameAndPassword(user, pass).orElse(null);
        }
    }

    // Send an otp only if the user is not already registered
    public boolean SendOTP(User user) {
        if (authRepo.findByEmail(user.getEmail()).isPresent()
                || authRepo.findByUsername(user.getUsername()).isPresent()) {
            return false;
        } else {
            OTPService.SendOTP(user);
            return true;
        }
    }

    // Verify the otp and save the user in the database
    public boolean VerifyAndRegister(User user, String otp) throws IOException {
        if (OTPService.VerifyOTP(user, otp)) {
            authRepo.save(user);
            EmailService.SendRegistrationEmail(user);
            return true;
        }
        return false;
    }

}
