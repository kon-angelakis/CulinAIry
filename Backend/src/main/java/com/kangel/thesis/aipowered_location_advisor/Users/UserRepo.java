package com.kangel.thesis.aipowered_location_advisor.Users;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends MongoRepository<User, ObjectId> {

    public Optional<User> findByEmail(String email);

    public Optional<User> findByUsername(String username);
}
