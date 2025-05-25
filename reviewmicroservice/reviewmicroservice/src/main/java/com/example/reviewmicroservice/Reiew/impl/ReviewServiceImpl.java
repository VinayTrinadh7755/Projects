package com.example.reviewmicroservice.Reiew.impl;

/*import com.example.springbootproject.Company.Company;
import com.example.springbootproject.Company.CompanyService;
import com.example.springbootproject.Reiew.Review;
import com.example.springbootproject.Reiew.ReviewRepository;
import com.example.springbootproject.Reiew.ReviewService;*/
import com.example.reviewmicroservice.Reiew.Review;
import com.example.reviewmicroservice.Reiew.ReviewRepository;
import com.example.reviewmicroservice.Reiew.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    /*
    @Autowired
    private CompanyService companyService;*/

    @Override
    public List<Review> getallReviews(Long companyId) {
        List<Review> reviews = reviewRepository.findByCompanyId(companyId); //not a default method, we need to declare it in review repository and implementation will be done during run time by reviewrepository
        return reviews;
    }

    @Override
    public boolean addReview(Long companyId,Review review) {
        //Company company = companyService.getCompanyById(companyId);
        if(companyId != null && review != null) {
            review.setCompanyId(companyId);
            reviewRepository.save(review);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElse(null);
    }

    @Override
    public boolean updateReview(Long reviewId, Review updatedReview) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review != null) {
            review.setTitle(updatedReview.getTitle());
            review.setDescription(updatedReview.getDescription());
            review.setRating(updatedReview.getRating());
            review.setCompanyId(updatedReview.getCompanyId());
            reviewRepository.save(review);
            return true;
        } else {
            return false;
        }
    }


    @Override
    public boolean deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review != null) {
            reviewRepository.delete(review);
            return true;
        } else {
            return false;
        }
    }





}
