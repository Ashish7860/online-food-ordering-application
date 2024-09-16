package com.nagarro.online_food_ordering_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "dishes")
@JsonIgnoreProperties("hibernateLazyInitializer")
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dishId;
    private String name;
    private String description;
    private String cuisine;
    private String availability;
    private double averageRating;
    private double price;
   // private boolean available;

    @OneToMany(mappedBy = "dish", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<DishReview> review;

    // Constructors, Getters, and Setters

    public Dish() {
        super();
    }

    public Dish(Long dishId, String name, double price, String description, String cuisine, String availability, double averageRating, List<DishReview> review) {
        this.dishId = dishId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.cuisine = cuisine;
        this.availability = availability;
        this.averageRating = averageRating;
        this.review = review;
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public double getPrice() {
        return price;
    }
    
//    public boolean isAvailable() {
//        return available;
//    }
//
//    public void setAvailable(boolean available) {
//        this.available = available;
//    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<DishReview> getReview() {
        return review;
    }

    public void setReview(List<DishReview> review) {
        this.review = review;
    }

}
