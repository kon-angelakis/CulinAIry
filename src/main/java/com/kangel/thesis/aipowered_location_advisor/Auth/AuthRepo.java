package com.kangel.thesis.aipowered_location_advisor.Auth;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kangel.thesis.aipowered_location_advisor.Users.User;

@Repository
public interface AuthRepo extends MongoRepository<User, ObjectId>{

    public Optional<User> findByUsernameAndPassword(String username, String password);

    public Optional<User> findByEmailAndPassword(String email, String password);

}
