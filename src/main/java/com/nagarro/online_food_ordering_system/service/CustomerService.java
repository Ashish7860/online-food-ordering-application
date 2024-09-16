package com.nagarro.online_food_ordering_system.service;

import com.nagarro.online_food_ordering_system.entity.Customer;

import java.util.List;

public interface CustomerService {
    Customer addCustomer(Customer customer);
    Customer addAdmin(Customer customer);
    void seedAdmin(Customer customer);
    List<Customer> getAllCustomers();
    Customer getCustomerById(Long customerId);
    Customer getCustomerByEmail(String email);
    Customer updateCustomer(Long customerId, Customer customer);
    void deleteCustomer(Long customerId);
    Customer getCustomerByUsername(String username);
}
