package com.nagarro.online_food_ordering_system.service;

import com.nagarro.online_food_ordering_system.entity.Order;
import com.nagarro.online_food_ordering_system.response.BillResponse;

import java.util.List;

public interface OrderService {
    Order placeOrder(Order order);
    List<Order> getOrderByCustomerId(Long customerId);
    Order getOrderByOrderId(Long orderId);
    BillResponse getBillOfOrder(Long orderId);
}
