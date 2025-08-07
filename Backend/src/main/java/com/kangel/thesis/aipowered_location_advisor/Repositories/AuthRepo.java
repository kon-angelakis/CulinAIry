package com.kangel.thesis.aipowered_location_advisor.Repositories;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kangel.thesis.aipowered_location_advisor.Models.User;

@Repository
public interface AuthRepo extends MongoRepository<User, ObjectId> {

    public Optional<User> findByEmail(String email);

    public Optional<User> findByUsername(String username);

}
