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

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, String> payload) {
        try{
            authService.register(new User(payload.get("firstname"), 
                                                payload.get("lastname"), 
                                                payload.get("email"), 
                                                payload.get("username"), 
                                                payload.get("password"), 
                                                payload.get("pfp")
                                            )
                                    );
            return new ResponseEntity<String>("User registered successfully", HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<String>("User registration failed", HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody Map<String, String> payload) {
        User loginedUser = authService.login(payload.get("user"), payload.get("pass"));
        if(loginedUser != null)
            return new ResponseEntity<String>("User login successfully", HttpStatus.OK);
        else
            return new ResponseEntity<String>("User login failed", HttpStatus.UNAUTHORIZED);
    }
    
}
