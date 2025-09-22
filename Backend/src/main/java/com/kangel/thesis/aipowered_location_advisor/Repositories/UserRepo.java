package com.kangel.thesis.aipowered_location_advisor.Repositories;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kangel.thesis.aipowered_location_advisor.Models.User;

@Repository
public interface UserRepo extends MongoRepository<User, ObjectId> {

    public Optional<User> findByEmail(String email);

    public Optional<User> findByUsername(String username);

    public Boolean existsByUsernameAndFavouritesContains(String username, String placeId);

    @Aggregation(pipeline = {
            "{ $match: { username: ?0 } }",
            "{ $unwind: \"$favourites\" }",
            "{ $lookup: { from: \"Places\", localField: \"favourites\", foreignField: \"_id\", as: \"place\" } }",
            "{ $unwind: \"$place\" }",
            "{ $group: { _id: \"$place.primaryTypeRaw\", count: { $sum: 1 } } }",
            "{ $sort: { count: -1 } }",
            "{ $limit: 3 }",
            "{ $project: { _id: 0, primaryTypeRaw: \"$_id\" } }"
    })
    List<String> findCuratedPlacesByUsername(String username);

}
