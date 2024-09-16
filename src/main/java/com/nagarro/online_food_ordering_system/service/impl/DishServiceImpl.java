package com.nagarro.online_food_ordering_system.service.impl;

import com.nagarro.online_food_ordering_system.entity.Dish;
import com.nagarro.online_food_ordering_system.entity.DishReview;
import com.nagarro.online_food_ordering_system.exception.BadRequestException;
import com.nagarro.online_food_ordering_system.exception.RecordAlreadyExistsException;
import com.nagarro.online_food_ordering_system.exception.RecordNotFoundException;
import com.nagarro.online_food_ordering_system.repository.DishRepository;
import com.nagarro.online_food_ordering_system.repository.DishReviewRepository;
import com.nagarro.online_food_ordering_system.service.DishService;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl implements DishService {
	Logger logger = LoggerFactory.getLogger(DishServiceImpl.class);
	private final DishRepository dishRepository;
	private final DishReviewRepository dishReviewRepository;

	public DishServiceImpl(DishRepository dishRepository, DishReviewRepository dishReviewRepository) {
		this.dishRepository = dishRepository;
		this.dishReviewRepository = dishReviewRepository;
	}
//	 @Override
//	    public boolean isDishAvailable(Long dishId) {
//	        Dish dish = getDishByDishId(dishId);
//	        return dish != null && dish.isAvailable();
//	    }
	
	@Override
	public Dish addDish(Dish dish) {
		Optional<Dish> optionalDish = dishRepository.findByName(dish.getName());
		if (optionalDish.isPresent()) {
			throw new RecordAlreadyExistsException("Dish Already Exists", HttpStatus.BAD_REQUEST.value());
		}
		if (dish.getCuisine().equalsIgnoreCase("CHINESE") || dish.getCuisine().equalsIgnoreCase("NORTH-INDIAN")
				|| dish.getCuisine().equalsIgnoreCase("SOUTH-INDIAN")
				|| dish.getCuisine().equalsIgnoreCase("CONTINENTAL")) {
			return dishRepository.save(dish);
		}
		throw new BadRequestException("CUISINE not valid", HttpStatus.BAD_REQUEST.value());
	}

	@Override
	public List<Dish> availableDishes(String cuisine) {
		List<Dish> dishes = dishRepository.findByCuisine(cuisine);
		if (dishes.isEmpty()) {
			throw new RecordNotFoundException("No Dish Available for cuisine " + cuisine, HttpStatus.NOT_FOUND.value());
		}
		return dishes.stream().filter(dish -> dish.getAvailability().equalsIgnoreCase("AVAILABLE"))
				.peek(this::updateAverageRating).collect(Collectors.toList());
	}

	@Override
	public Dish getDishByDishId(Long dishId) {
		Optional<Dish> optionalDish = dishRepository.findById(dishId);
		if (optionalDish.isEmpty()) {
			throw new RecordNotFoundException("Dish not found with id: " + dishId, HttpStatus.NOT_FOUND.value());
		}
		Dish dish = optionalDish.get();
		updateAverageRating(dish);
		return dish;
	}

	@Override
	public List<Dish> sortedByRating() {
		List<Dish> dishes = dishRepository.findAll();
		dishes.forEach(this::updateAverageRating);
		return dishes.stream().sorted(Comparator.comparingDouble(Dish::getAverageRating).reversed())
				.collect(Collectors.toList());
	}

	private void updateAverageRating(Dish dish) {
		List<DishReview> reviews = dish.getReview();
		if (reviews != null && !reviews.isEmpty()) {
			double averageRating = reviews.stream().mapToDouble(DishReview::getRating).average().orElse(0.0);
			dish.setAverageRating(averageRating);
			dishRepository.save(dish);
		} else {
			dish.setAverageRating(0.0);
			dishRepository.save(dish);
		}
	}

	@Override
	@Transactional
	public Dish updateDish(Dish dish) {
		return dishRepository.save(dish);
	}

}
