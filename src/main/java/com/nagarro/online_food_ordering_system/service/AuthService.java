package com.nagarro.online_food_ordering_system.service;

import com.nagarro.online_food_ordering_system.request.LoginRequest;

public interface AuthService {
    String authenticateUser(LoginRequest loginRequest);
}
