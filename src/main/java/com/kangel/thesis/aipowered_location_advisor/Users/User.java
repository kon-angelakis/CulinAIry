package com.kangel.thesis.aipowered_location_advisor.Users;

import com.kangel.thesis.aipowered_location_advisor.Places.*;
import com.kangel.thesis.aipowered_location_advisor.Reviews.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private ObjectId id;
    
    private Optional<String> firstName;
    private Optional<String> lastName;
    private String email;
    private String username;
    private String password;
    
    private Optional<String> profilePicture;

    private List<Place> history;
    private List<Place> visited;
    private List<Review> reviews;

    //Login constructor for external login like google
    public User(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }

    //Register constructors profilePictu is optional when registering
    public User(String username, String password, String email, String firstName, String lastName, String profilePicture){
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = Optional.ofNullable(firstName);
        this.lastName = Optional.ofNullable(lastName);
        this.profilePicture = Optional.ofNullable(profilePicture);
    }

    public User(String username, String password, String email, String firstName, String lastName){
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = Optional.ofNullable(firstName);
        this.lastName = Optional.ofNullable(lastName);
    }

}