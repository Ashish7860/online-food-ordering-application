package com.nagarro.online_food_ordering_system.repository;

import com.nagarro.online_food_ordering_system.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByCuisine(String cuisine);
    Optional<Dish> findByName(String name);
}
