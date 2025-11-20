package com.kangel.thesis.aipowered_location_advisor.Repositories;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kangel.thesis.aipowered_location_advisor.Models.UserInteraction;
import com.kangel.thesis.aipowered_location_advisor.Models.Enums.InteractionType;

@Repository
public interface UserInteractionRepo extends MongoRepository<UserInteraction, ObjectId> {

        public Optional<UserInteraction> findByUserIdAndPlaceIdAndType(ObjectId userId, String placeId,
                        InteractionType type);

        public Page<UserInteraction> findAllByUserIdAndType(ObjectId userId, InteractionType type,
                        Pageable pageable);

        public List<UserInteraction> findAllByPlaceIdAndType(String placeId, InteractionType type);

        public List<UserInteraction> findAllByUserIdAndType(ObjectId userId, InteractionType type);

        @Aggregation(pipeline = {
                        "{ $match: { type: 'CLICKED' } }",
                        "{ $group: { _id: '$placeId', count: { $sum: 1 } } }",
                        "{ $sort: { count: -1 } }",
                        "{ $project: { _id: 0, placeId: '$_id' } }"
        })
        List<String> findMostClickedPlaceIds();

}
