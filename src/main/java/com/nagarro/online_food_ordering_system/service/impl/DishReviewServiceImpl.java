package com.nagarro.online_food_ordering_system.service.impl;

import com.nagarro.online_food_ordering_system.entity.Dish;
import com.nagarro.online_food_ordering_system.entity.DishReview;
import com.nagarro.online_food_ordering_system.exception.BadRequestException;
import com.nagarro.online_food_ordering_system.repository.DishReviewRepository;
import com.nagarro.online_food_ordering_system.repository.OrderRepository;
import com.nagarro.online_food_ordering_system.service.DishReviewService;
import com.nagarro.online_food_ordering_system.service.DishService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishReviewServiceImpl implements DishReviewService {
	private final DishReviewRepository dishReviewRepository;
	private final DishService dishService;
	private final OrderRepository orderRepository;

	public DishReviewServiceImpl(DishReviewRepository dishReviewRepository, DishService dishService,
			OrderRepository orderRepository) {
		this.dishReviewRepository = dishReviewRepository;
		this.dishService = dishService;
		this.orderRepository = orderRepository;
	}

	@Override
	@Transactional
	public DishReview addReviews(DishReview review, Long dishId, Long customerId) {
		Dish dish = dishService.getDishByDishId(dishId);

		// Check if the customer has ordered this dish
		if (!orderRepository.existsByCustomerIdAndDishId(customerId, dishId)) {
		    throw new BadRequestException("Customer has not ordered this dish", HttpStatus.BAD_REQUEST.value());
		}
		
		// Check if the customer has already reviewed this dish
		if (dishReviewRepository.existsByDish_DishIdAndCustomerId(dishId, customerId)) {
			throw new BadRequestException("Customer has already reviewed this dish", HttpStatus.BAD_REQUEST.value());
		}

		review.setDish(dish);
		review.setCustomerId(customerId);
		DishReview savedReview = dishReviewRepository.save(review);

		updateAverageRating(dish);

		return savedReview;
	}

	private void updateAverageRating(Dish dish) {
		List<DishReview> reviews = dishReviewRepository.findByDish_DishId(dish.getDishId());
		double averageRating = reviews.stream().mapToDouble(DishReview::getRating).average().orElse(0.0);

		dish.setAverageRating(averageRating);
		dishService.updateDish(dish);
	}
}