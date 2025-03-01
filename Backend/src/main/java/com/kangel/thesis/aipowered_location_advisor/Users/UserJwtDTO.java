package com.kangel.thesis.aipowered_location_advisor.Users;

import lombok.AllArgsConstructor;
import lombok.Data;

// DTO class to be used in the jwt token
@Data
@AllArgsConstructor
public class UserJwtDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String pfp = "";

}
