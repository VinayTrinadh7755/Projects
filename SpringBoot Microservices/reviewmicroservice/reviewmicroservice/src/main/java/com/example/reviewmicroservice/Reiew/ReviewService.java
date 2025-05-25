package com.example.reviewmicroservice.Reiew;

import java.util.List;

public interface ReviewService {
    List<Review> getallReviews(Long companyId);
    boolean addReview(Long companyId,Review review);
    Review getReview(Long reviewId);
    boolean updateReview(Long reviewId,Review review);
    boolean deleteReview(Long reviewId);
}
