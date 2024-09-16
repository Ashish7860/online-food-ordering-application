package com.nagarro.online_food_ordering_system.seeder;

import com.nagarro.online_food_ordering_system.entity.Customer;
import com.nagarro.online_food_ordering_system.entity.Role;
import com.nagarro.online_food_ordering_system.service.CustomerService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder {
    private final CustomerService customerService;

    public AdminSeeder(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostConstruct
    public void seedDatabase() {
        Customer adminUser = getHardcodedData();
        customerService.seedAdmin(adminUser);
    }

    private Customer getHardcodedData() {
        return new Customer(1L, "Nagarro", "Nagarro", "nagarro@nagarro.com", "Nagarro22", Role.ADMIN);
    }
}
