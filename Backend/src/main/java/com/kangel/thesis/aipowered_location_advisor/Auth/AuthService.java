package com.kangel.thesis.aipowered_location_advisor.Auth;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    @Autowired
    public AuthService(AuthRepo authRepo, RegistrationEmailSender rEmailSender, EmailOTP eOTP,
            BCryptPasswordEncoder encoder, AuthenticationManager authManager, JwtService jwtService) {
        this.authRepo = authRepo;
        this.rEmailSender = rEmailSender;
        this.eOTP = eOTP;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    //Generates and returns a map of the login status and the jwt token
    public Map<String, Object> Login(String user, String pass) {
        try {
            Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(user, pass));
                return Map.of(
                    "status", "Successful",
                    "token", jwtService.GenerateToken(UserExists(user)),
                    "statusCode", HttpStatus.OK
                );

        } catch (AuthenticationException e){
            return Map.of(
                "status", "Failed",
                "token", "-",
                "statusCode", HttpStatus.UNAUTHORIZED
            );
        }
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

    public User UserExists(String user) {
        User loginedUser = user.contains("@") ? authRepo.findByEmail(user).orElse(null)
                : authRepo.findByUsername(user).orElse(null);
        return loginedUser;
    }


}
