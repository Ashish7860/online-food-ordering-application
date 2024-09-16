package com.nagarro.online_food_ordering_system.service;

import com.nagarro.online_food_ordering_system.entity.Dish;

import java.util.List;

public interface DishService {
    Dish addDish(Dish dish);
    List<Dish> availableDishes(String cuisine);
    Dish getDishByDishId(Long dishId);
    List<Dish> sortedByRating();
    Dish updateDish(Dish dish);
   // boolean isDishAvailable(Long dishId);
}
