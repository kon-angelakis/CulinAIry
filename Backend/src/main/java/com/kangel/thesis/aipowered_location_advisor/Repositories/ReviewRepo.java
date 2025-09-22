package com.kangel.thesis.aipowered_location_advisor.Repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kangel.thesis.aipowered_location_advisor.Models.Review;

@Repository
public interface ReviewRepo extends MongoRepository<Review, ObjectId> {

    @Aggregation(pipeline = {
            "{ '$match': { 'placeId': ?0 } }",
            "{ '$sample': { 'size': 10 } }",
    })
    public List<Review> findSampleReviewsByplaceId(String id);

    Page<Review> findAllByAuthorId(ObjectId authorId, Pageable pageable);

    public List<Review> findAllReviewsByAuthorIdAndPlaceId(ObjectId authorId, String placeId);

}
