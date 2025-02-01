package com.kangel.thesis.aipowered_location_advisor.Auth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kangel.thesis.aipowered_location_advisor.Services.Email.RegistrationEmailSender;
import com.kangel.thesis.aipowered_location_advisor.Services.OTP.EmailOTP;
import com.kangel.thesis.aipowered_location_advisor.Users.User;

@Service
public class AuthService {

    private final AuthRepo authRepo;
    private final RegistrationEmailSender rEmailSender;
    private final EmailOTP eOTP;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public AuthService(AuthRepo authRepo, RegistrationEmailSender rEmailSender, EmailOTP eOTP,
            BCryptPasswordEncoder encoder) {
        this.authRepo = authRepo;
        this.rEmailSender = rEmailSender;
        this.eOTP = eOTP;
        this.encoder = encoder;
    }

    public User Login(String user, String pass) {
        // Get the user accordingly via email or username and if the password(newly
        // encoded) matches the encoded one login else return null
        User loginedUser = user.contains("@") ? authRepo.findByEmail(user).orElse(null)
                : authRepo.findByUsername(user).orElse(null);

        if (loginedUser != null && encoder.matches(pass, loginedUser.getPassword()))
            return loginedUser;
        return null;
    }

    // Send an otp only if the user is not already registered
    public boolean SendOTP(User user) {
        if (authRepo.findByEmail(user.getEmail()).isPresent()
                || authRepo.findByUsername(user.getUsername()).isPresent()) {
            return false;
        } else {
            eOTP.SendOTP(user);
            return true;
        }
    }

    // Verify the otp via email and save the user in the database
    public boolean VerifyAndRegister(User user, String otp) throws IOException {
        if (eOTP.VerifyOTP(user, otp)) {
            // Encode the users password using the BCryptPasswordEncoder before saving
            user.setPassword(encoder.encode(user.getPassword()));
            authRepo.save(user);
            rEmailSender.SendEmail(user);
            return true;
        }
        return false;
    }

}
