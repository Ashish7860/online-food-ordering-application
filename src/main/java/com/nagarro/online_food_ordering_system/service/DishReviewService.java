package com.nagarro.online_food_ordering_system.service;

import com.nagarro.online_food_ordering_system.entity.DishReview;

public interface DishReviewService {
    DishReview addReviews(DishReview review, Long dishId, Long customerId);
}
