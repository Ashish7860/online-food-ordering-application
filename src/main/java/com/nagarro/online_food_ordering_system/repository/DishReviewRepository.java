package com.nagarro.online_food_ordering_system.repository;

import com.nagarro.online_food_ordering_system.entity.DishReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishReviewRepository extends JpaRepository<DishReview,Integer> {
    List<DishReview> findByDish_DishId(Long dishId);
    boolean existsByDish_DishIdAndCustomerId(Long dishId, Long customerId);

}
