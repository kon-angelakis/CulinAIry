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

        // Join 3 tables Users, Places, UserInteractions and find the users top3
        // favourite categories to recommend more places in these categories
        @Aggregation(pipeline = {
                        "{ $match: { _id: ?0 } }",
                        "{ $lookup: { " +
                                        "from: 'Interactions', " +
                                        "localField: '_id', " +
                                        "foreignField: 'userId', " +
                                        "as: 'interactions' " +
                                        "} }",
                        "{ $unwind: '$interactions' }",
                        "{ $match: { 'interactions.type': 'FAVOURITES' } }",
                        "{ $lookup: { " +
                                        "from: 'Places', " +
                                        "localField: 'interactions.placeId', " +
                                        "foreignField: '_id', " +
                                        "as: 'place' " +
                                        "} }",
                        "{ $unwind: '$place' }",
                        "{ $group: { " +
                                        "_id: '$place.primaryTypeRaw', " +
                                        "count: { $sum: 1 } " +
                                        "} }",
                        "{ $sort: { count: -1 } }",
                        "{ $limit: 3 }",
                        "{ $project: { _id: 0, primaryTypeRaw: '$_id' } }"
        })
        List<String> findCuratedPlacesById(ObjectId userId);

}
