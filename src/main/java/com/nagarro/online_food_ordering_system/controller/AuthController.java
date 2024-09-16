package com.nagarro.online_food_ordering_system.controller;

import com.nagarro.online_food_ordering_system.entity.Customer;
import com.nagarro.online_food_ordering_system.request.LoginRequest;
import com.nagarro.online_food_ordering_system.response.LoginResponse;
import com.nagarro.online_food_ordering_system.service.AuthService;
import com.nagarro.online_food_ordering_system.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/auth")
public class AuthController {

    private final AuthService authService;
    private final CustomerService customerService;

    // Constructor Autowiring 
    public AuthController(AuthService authService, CustomerService customerService) {
        this.authService = authService;
        this.customerService = customerService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        String token = authService.authenticateUser(loginRequest);
        Customer existingCustomer = customerService.getCustomerByEmail(loginRequest.getEmail());
        LoginResponse loginResponse = new LoginResponse(existingCustomer, token);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }
}
