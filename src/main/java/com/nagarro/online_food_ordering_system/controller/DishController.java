package com.nagarro.online_food_ordering_system.controller;

import com.nagarro.online_food_ordering_system.entity.Dish;
import com.nagarro.online_food_ordering_system.entity.DishReview;
import com.nagarro.online_food_ordering_system.exception.BadRequestException;
import com.nagarro.online_food_ordering_system.exception.UnauthorizedAccessException;
import com.nagarro.online_food_ordering_system.service.DishService;
import com.nagarro.online_food_ordering_system.service.DishReviewService;
import com.nagarro.online_food_ordering_system.util.JwtTokenUtil;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/api")
public class DishController {

	private final DishService dishService;
	private final DishReviewService dishReviewService;
	private final JwtTokenUtil jwtTokenUtil;

	public DishController(DishService dishService, DishReviewService dishReviewService, JwtTokenUtil jwtTokenUtil) {
		this.dishService = dishService;
		this.dishReviewService = dishReviewService;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	// PreAuthorize permission for Admin, so that he can add dishes
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/dishes/add")
	public ResponseEntity<Dish> addDish(@RequestBody Dish dish) {
		Dish addedDish = dishService.addDish(dish);
		return new ResponseEntity<>(addedDish, HttpStatus.CREATED);
	}

//	 @PreAuthorize("hasAuthority('ADMIN')")
//	    @PutMapping("/dishes/{dishId}/availability")
//	    public ResponseEntity<Dish> updateDishAvailability(@PathVariable Long dishId, @RequestParam boolean available) {
//	        Dish dish = dishService.getDishByDishId(dishId);
//	        dish.setAvailable(available);
//	        Dish updatedDish = dishService.updateDish(dish);
//	        return new ResponseEntity<>(updatedDish, HttpStatus.OK);
//	    }
	
	
	// available dishes by cuisine
	@GetMapping("/dishes/cuisine/{cuisine}")
	public ResponseEntity<List<Dish>> availableDishesOfCuisine(@PathVariable String cuisine) {
		List<Dish> dishes = dishService.availableDishes(cuisine);
		return new ResponseEntity<>(dishes, HttpStatus.OK);
	}

	// dish by its ID
	@GetMapping("/dishes/id/{dishId}")
	public ResponseEntity<Dish> getDishByDishId(@PathVariable Long dishId) {
		Dish dish = dishService.getDishByDishId(dishId);
		return new ResponseEntity<>(dish, HttpStatus.OK);
	}

	// Sort dishes by rating
	@GetMapping("/dishes/rating")
	public ResponseEntity<List<Dish>> sortedByRating() {
		List<Dish> dishes = dishService.sortedByRating();
		return new ResponseEntity<>(dishes, HttpStatus.OK);
	}

	// Adding reviews of dishes for a specific customer
	@PostMapping("/reviews/{dishId}")
	public ResponseEntity<DishReview> addReview(@RequestBody DishReview review, @PathVariable Long dishId,
			HttpServletRequest request) {

		String authorizationHeader = request.getHeader("Authorization");

		// Checking if the Authorization header is missing or not properly formatted
		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			throw new BadRequestException("Authorization token missing or invalid", HttpStatus.BAD_REQUEST.value());
		}

		String token = authorizationHeader.substring(7);

		// Extract customerId from token
		Long tokenCustomerId;
		try {
			tokenCustomerId = jwtTokenUtil.getUserIdFromToken(token);
		} catch (Exception e) {
			throw new BadRequestException("Invalid token", HttpStatus.BAD_REQUEST.value());
		}

		// Extract customerId from request parameters
		Long requestCustomerId = Long.parseLong(request.getParameter("customerId"));

		// Validate that the customerId from the token matches the customerId from the request
		if (!tokenCustomerId.equals(requestCustomerId)) {
			throw new UnauthorizedAccessException(
					"User ID does not match the token, you cannot review this dish by this account",
					HttpStatus.UNAUTHORIZED.value());
		}

		DishReview addedReview = dishReviewService.addReviews(review, dishId, tokenCustomerId);
		return new ResponseEntity<>(addedReview, HttpStatus.CREATED);
	}

}
