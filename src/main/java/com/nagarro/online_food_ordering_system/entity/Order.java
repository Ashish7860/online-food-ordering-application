package com.nagarro.online_food_ordering_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "orders")
@JsonPropertyOrder({"orderNumber", "orderDate", "totalPrice", "deliveryAddress", "deliveryStatus", "orderStatus", "customerId", "dishes"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "dishes"})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderNumber;

    private LocalDate orderDate;
    private double totalPrice;
    private String deliveryAddress;
    private String deliveryStatus;
    private String orderStatus;
    private Long customerId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DishOrder> dishes = new ArrayList<>();

    // Constructors, getters, and setters

    @JsonProperty("dishes")
    public List<Map<String, Object>> getDishDetails() {
        if (dishes == null) {
            return new ArrayList<>();
        }
        return dishes.stream().map(dishOrder -> {
            Map<String, Object> dishMap = new HashMap<>();
            dishMap.put("dishId", dishOrder.getDish().getDishId());
            dishMap.put("dishName", dishOrder.getDish().getName());
            dishMap.put("quantity", dishOrder.getQuantity());
            return dishMap;
        }).toList();
    }
    
    public boolean containsDish(Long dishId) {
        return dishes.stream().anyMatch(dishOrder -> dishOrder.getDish().getDishId().equals(dishId));
    }

    public void calculateTotalPrice() {
        this.totalPrice = dishes.stream()
            .mapToDouble(dishOrder -> dishOrder.getDish().getPrice() * dishOrder.getQuantity())
            .sum();
    }

    // Add getters and setters for orderStatus
    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

	public Long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public List<DishOrder> getDishes() {
		return dishes;
	}

	public void setDishes(List<DishOrder> dishes) {
		this.dishes = dishes;
	}
}