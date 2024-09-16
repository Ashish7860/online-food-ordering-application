package com.nagarro.online_food_ordering_system.controller;

import com.nagarro.online_food_ordering_system.entity.Dish;
import com.nagarro.online_food_ordering_system.entity.DishOrder;
import com.nagarro.online_food_ordering_system.entity.Order;
import com.nagarro.online_food_ordering_system.response.BillResponse;
import com.nagarro.online_food_ordering_system.service.DishService;
import com.nagarro.online_food_ordering_system.service.OrderService;
import com.nagarro.online_food_ordering_system.util.JwtTokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/api/orders")
public class OrderController {

	private final DishService dishService;
	private final OrderService orderService;
	private final JwtTokenUtil jwtTokenUtil;

	@Autowired
	public OrderController(OrderService orderService, DishService dishService, JwtTokenUtil jwtTokenUtil) {
		this.orderService = orderService;
		this.dishService = dishService;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@PreAuthorize("hasAuthority('USER')")
	@PostMapping("/place")
	public ResponseEntity<Map<String, Object>> placeOrder(@RequestHeader("Authorization") String token,
			@RequestBody Map<String, Object> orderRequest) {
		// Extract user ID from token
		String tokenWithoutBearer = token.substring(7); 
		Long userIdFromToken = jwtTokenUtil.getUserIdFromToken(tokenWithoutBearer);
		Long customerIdFromRequest = Long.valueOf(orderRequest.get("customerId").toString());

		// Validate that the user ID from the token matches the customer ID in the request
		if (!userIdFromToken.equals(customerIdFromRequest)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body(Map.of("message", "User ID does not match with token"));
		}

		
		Order order = new Order();
		order.setDeliveryAddress((String) orderRequest.get("deliveryAddress"));
		order.setDeliveryStatus("Pending");
		order.setOrderStatus("Placed");
		order.setCustomerId(customerIdFromRequest);

		List<Map<String, Object>> dishesRequest = (List<Map<String, Object>>) orderRequest.get("dishes");
		List<DishOrder> dishOrders = new ArrayList<>();

		if (dishesRequest != null && !dishesRequest.isEmpty()) {
			for (Map<String, Object> dishRequest : dishesRequest) {
				Long dishId = Long.valueOf(dishRequest.get("dishId").toString());
				int quantity = Integer.parseInt(dishRequest.get("quantity").toString());

				Dish dish = dishService.getDishByDishId(dishId);
				DishOrder dishOrder = new DishOrder(order, dish, quantity);
				dishOrders.add(dishOrder);
			}
			order.setDishes(dishOrders);
			order.calculateTotalPrice();
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "No dishes in the order"));
		}

		Order placedOrder = orderService.placeOrder(order);

		Map<String, Object> response = new HashMap<>();
		response.put("orderNumber", placedOrder.getOrderNumber());
		response.put("totalPrice", placedOrder.getTotalPrice());
		response.put("deliveryAddress", placedOrder.getDeliveryAddress());
		response.put("deliveryStatus", placedOrder.getDeliveryStatus());
		response.put("orderStatus", placedOrder.getOrderStatus());
		response.put("customerId", placedOrder.getCustomerId());

		List<Map<String, Object>> dishesResponse = new ArrayList<>();
		for (DishOrder dishOrder : placedOrder.getDishes()) {
			Map<String, Object> dishResponse = new HashMap<>();
			dishResponse.put("dishId", dishOrder.getDish().getDishId());
			dishResponse.put("dishName", dishOrder.getDish().getName());
			dishResponse.put("quantity", dishOrder.getQuantity());
			dishesResponse.add(dishResponse);
		}
		response.put("dishes", dishesResponse);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/id/{orderId}")
	public ResponseEntity<Map<String, Object>> getOrderByOrderId(@PathVariable Long orderId) {
		Order order = orderService.getOrderByOrderId(orderId);

		Map<String, Object> response = new HashMap<>();
		response.put("orderNumber", order.getOrderNumber());
		response.put("totalPrice", order.getTotalPrice());
		response.put("deliveryAddress", order.getDeliveryAddress());
		response.put("deliveryStatus", order.getDeliveryStatus());
		response.put("orderStatus", order.getOrderStatus());
		response.put("customerId", order.getCustomerId());

		List<Map<String, Object>> dishesResponse = new ArrayList<>();
		for (DishOrder dishOrder : order.getDishes()) {
			Map<String, Object> dishResponse = new HashMap<>();
			dishResponse.put("dishId", dishOrder.getDish().getDishId());
			dishResponse.put("dishName", dishOrder.getDish().getName()); // Changed from getDishName() to getName()
			dishResponse.put("quantity", dishOrder.getQuantity());
			dishesResponse.add(dishResponse);
		}
		response.put("dishes", dishesResponse);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/customer/{customerId}")
	public ResponseEntity<List<Map<String, Object>>> getOrderByCustomerId(@PathVariable Long customerId) {
		List<Order> orders = orderService.getOrderByCustomerId(customerId);
		List<Map<String, Object>> response = new ArrayList<>();

		for (Order order : orders) {
			Map<String, Object> orderResponse = new HashMap<>();
			orderResponse.put("orderNumber", order.getOrderNumber());
			orderResponse.put("totalPrice", order.getTotalPrice());
			orderResponse.put("deliveryAddress", order.getDeliveryAddress());
			orderResponse.put("deliveryStatus", order.getDeliveryStatus());
			orderResponse.put("orderStatus", order.getOrderStatus());
			orderResponse.put("customerId", order.getCustomerId());

			List<Map<String, Object>> dishesResponse = new ArrayList<>();
			for (DishOrder dishOrder : order.getDishes()) {
				Map<String, Object> dishResponse = new HashMap<>();
				dishResponse.put("dishId", dishOrder.getDish().getDishId());
				dishResponse.put("dishName", dishOrder.getDish().getName()); 
				dishResponse.put("quantity", dishOrder.getQuantity());
				dishesResponse.add(dishResponse);
			}
			orderResponse.put("dishes", dishesResponse);

			response.add(orderResponse);
		}

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/bill/{orderId}")
	public ResponseEntity<BillResponse> getBillByOrderId(@PathVariable Long orderId) {
		BillResponse bill = orderService.getBillOfOrder(orderId);
		return new ResponseEntity<>(bill, HttpStatus.OK);
	}
}