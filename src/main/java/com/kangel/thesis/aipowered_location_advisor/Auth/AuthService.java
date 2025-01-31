package com.kangel.thesis.aipowered_location_advisor.Auth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kangel.thesis.aipowered_location_advisor.Services.EmailService;
import com.kangel.thesis.aipowered_location_advisor.Users.User;


@Service
public class AuthService {
    @Autowired 
    private AuthRepo authRepo;

    public User login(String user, String pass){
        //Check for login with email or username
        if((user.toString().contains("@"))){
            return authRepo.findByEmailAndPassword(user, pass).orElse(null);
        }else{
            return authRepo.findByUsernameAndPassword(user, pass).orElse(null);
        }
    }

    public void register(User user) throws IOException{
        authRepo.save(user);
        EmailService.SendRegistrationEmail(user);
    }

}
