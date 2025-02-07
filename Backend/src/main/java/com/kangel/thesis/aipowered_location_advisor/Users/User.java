package com.kangel.thesis.aipowered_location_advisor.Users;

import com.kangel.thesis.aipowered_location_advisor.Places.*;
import com.kangel.thesis.aipowered_location_advisor.Reviews.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private ObjectId id;

    private String firstName;
    private String lastName;

    private String email;
    private String username;
    private String password;

    private String pfp = "";

    private List<Place> history = List.of();
    private List<Place> visited = List.of();
    private List<Place> favourites = List.of();
    private List<Review> reviews = List.of();

    @CreatedDate
    private final LocalDateTime dateCreated = LocalDateTime.now();

    public User(String firstName, String lastName, String email, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
    }

}