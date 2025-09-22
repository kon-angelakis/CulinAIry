package com.kangel.thesis.aipowered_location_advisor.Services;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.kangel.thesis.aipowered_location_advisor.Models.Review;
import com.kangel.thesis.aipowered_location_advisor.Repositories.ReviewRepo;

@Service
public class ReviewService {

    private final ReviewRepo reviewRepo;

    public ReviewService(ReviewRepo reviewRepo) {
        this.reviewRepo = reviewRepo;
    }

    public Review SaveReview(Review review) {
        return reviewRepo.save(review);
    }

    public List<Review> SaveReviews(List<Review> reviews) {
        return reviewRepo.saveAll(reviews);
    }

    public List<Review> FindByPlaceId(String id) {
        return reviewRepo.findSampleReviewsByplaceId(id);
    }

    public Page<Review> FindByAuthorId(ObjectId id, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return reviewRepo.findAllByAuthorId(id, pageable);
    }

    public boolean ExistsByAuthorIdAndPlaceId(ObjectId authorId, String placeId) {
        return reviewRepo.findAllReviewsByAuthorIdAndPlaceId(authorId, placeId).size() > 0;
    }

    public Review FindMyReview(ObjectId authorId, String placeId) {
        List<Review> reviews = reviewRepo.findAllReviewsByAuthorIdAndPlaceId(authorId, placeId);
        return reviews.isEmpty() ? null : reviews.get(0);
    }

}
