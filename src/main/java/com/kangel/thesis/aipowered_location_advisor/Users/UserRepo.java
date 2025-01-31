package com.kangel.thesis.aipowered_location_advisor.Users;


import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepo extends MongoRepository<User, ObjectId>{


}
