package com.nagarro.online_food_ordering_system.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;


@Entity
@JsonIgnoreProperties("hibernateLazyInitializer")

public class DishReview {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int reviewId;
    private String review;
    private double rating;
    private Long customerId;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "dish_id")
    @JsonBackReference
    private Dish dish;

	public int getReviewId() {
		return reviewId;
	}

	public void setReviewId(int reviewId) {
		this.reviewId = reviewId;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public Dish getDish() {
		return dish;
	}

	public void setDish(Dish dish) {
		this.dish = dish;
	}

	public DishReview(int reviewId, String review, double rating, Dish dish) {
		super();
		this.reviewId = reviewId;
		this.review = review;
		this.rating = rating;
		this.dish = dish;
	}

	public DishReview() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

    
    
}
