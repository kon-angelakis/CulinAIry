package com.kangel.thesis.aipowered_location_advisor.Users;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<List<User>>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("{retrievaldata}")
    public ResponseEntity<Optional<User>> getUser(@PathVariable String retrievaldata) {
        Optional<User> user;
        if (retrievaldata.toString().contains("@")) {
            user = userService.getUserByEmail(retrievaldata.toString());
        } else {
            user = userService.getUserByUsername(retrievaldata.toString());
        }
        return new ResponseEntity<Optional<User>>(user, HttpStatus.OK);
        }

}

