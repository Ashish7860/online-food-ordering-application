package com.nagarro.online_food_ordering_system.service.impl;

import com.nagarro.online_food_ordering_system.entity.Customer;
import com.nagarro.online_food_ordering_system.entity.Dish;
import com.nagarro.online_food_ordering_system.entity.DishOrder;
import com.nagarro.online_food_ordering_system.entity.Order;
import com.nagarro.online_food_ordering_system.exception.BadRequestException;
import com.nagarro.online_food_ordering_system.exception.EmptyInputException;
import com.nagarro.online_food_ordering_system.exception.RecordNotFoundException;
import com.nagarro.online_food_ordering_system.repository.OrderRepository;
import com.nagarro.online_food_ordering_system.response.BillResponse;
import com.nagarro.online_food_ordering_system.service.CustomerService;
import com.nagarro.online_food_ordering_system.service.DishService;
import com.nagarro.online_food_ordering_system.service.OrderService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final DishService dishService;

    public OrderServiceImpl(OrderRepository orderRepository, CustomerService customerService, DishService dishService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.dishService = dishService;
    }

    @Override
    public Order placeOrder(Order order) {
        if(order.getCustomerId() == null || order.getDeliveryAddress().isBlank()){
            throw new EmptyInputException("Input cannot be null!!", HttpStatus.BAD_REQUEST.value());
        }
        
        if (order.getDishes() != null && !order.getDishes().isEmpty()) {
            for (DishOrder dishOrder : order.getDishes()) {
                Dish dish = dishService.getDishByDishId(dishOrder.getDish().getDishId());
                dishOrder.setDish(dish);
                dishOrder.setOrder(order);
            }
        } else {
            throw new EmptyInputException("Order must contain at least one dish", HttpStatus.BAD_REQUEST.value());
        }
//    @Override
//    public Order placeOrder(Order order) {
//        if(order.getCustomerId() == null || order.getDeliveryAddress().isBlank()){
//            throw new EmptyInputException("Input cannot be null!!", HttpStatus.BAD_REQUEST.value());
//        }
//        
//        if (order.getDishes() != null && !order.getDishes().isEmpty()) {
//            for (DishOrder dishOrder : order.getDishes()) {
//                Dish dish = dishService.getDishByDishId(dishOrder.getDish().getDishId());
//                if (!dishService.isDishAvailable(dish.getDishId())) {
//                    throw new BadRequestException("Dish " + dish.getName() + " Dish is present in the list but not available to order. Please revise your order and place again.", HttpStatus.BAD_REQUEST.value());
//                }
//                dishOrder.setDish(dish);
//                dishOrder.setOrder(order);
//            }
//        } else {
//            throw new EmptyInputException("Order must contain at least one dish", HttpStatus.BAD_REQUEST.value());
//        }
        
        order.calculateTotalPrice();
        order.setOrderDate(LocalDate.now());
        order.setDeliveryStatus("Pending");
        order.setOrderStatus("Placed");
        
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrderByCustomerId(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        if (orders.isEmpty()) {
            throw new RecordNotFoundException("No orders found for customer with id: " + customerId, HttpStatus.NOT_FOUND.value());
        }
        return orders;
    }

    @Override
    public Order getOrderByOrderId(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if(optionalOrder.isEmpty()){
            throw new RecordNotFoundException("Order not found with id: " + orderId, HttpStatus.NOT_FOUND.value());
        }
        return optionalOrder.get();
    }

    @Override
    public BillResponse getBillOfOrder(Long orderId) {
        // Checking if the user with the given userId already exists or not
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if(optionalOrder.isEmpty()){
            throw new RecordNotFoundException("Order not found with id: " + orderId, HttpStatus.NOT_FOUND.value());
        }
        Customer customer = customerService.getCustomerById(optionalOrder.get().getCustomerId());
        BillResponse billResponse = new BillResponse();
        billResponse.setOrder(optionalOrder.get());
        billResponse.setCustomer(customer);
        return billResponse;
    }
}
