package com.nagarro.online_food_ordering_system.repository;

import com.nagarro.online_food_ordering_system.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	  @Query("SELECT o FROM Order o WHERE o.customerId = :customerId")
	    List<Order> findByCustomerId(Long customerId);

	    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM Order o JOIN o.dishes d WHERE o.customerId = :customerId AND d.dish.dishId = :dishId")
	    boolean existsByCustomerIdAndDishId(Long customerId, Long dishId);

}

