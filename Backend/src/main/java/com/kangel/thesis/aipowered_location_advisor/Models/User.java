package com.kangel.thesis.aipowered_location_advisor.Models;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.kangel.thesis.aipowered_location_advisor.Models.Records.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private ObjectId id;
    private boolean isVerified;
    private String verificationCode;

    private String firstName;
    private String lastName;

    private String email;
    private String username;
    private String password;
    private String registration_method; // Legacy or OAuth2

    private String pfp = "";

    private List<Place> history = List.of();
    private List<Place> visited = List.of();
    private List<Place> favourites = List.of();
    private List<Review> reviews = List.of();

    @CreatedDate
    private final LocalDateTime date_created = LocalDateTime.now();

    // Legacy (alternatively use the same constructor but have the password be null
    // on oauth2 but whatever)
    public User(String firstName, String lastName, String email, String username, String password, String pfp,
            String registration_method) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.pfp = pfp;
        this.registration_method = registration_method;
    }

    // OAuth2
    public User(String firstName, String lastName, String email, String username, String pfp,
            String registration_method) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.pfp = pfp;
        this.registration_method = registration_method;
    }

    public UserDTO ToUserDTO() {
        return new UserDTO(firstName, lastName, email, username, pfp);
    }

}