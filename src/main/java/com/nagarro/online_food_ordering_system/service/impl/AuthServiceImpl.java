package com.nagarro.online_food_ordering_system.service.impl;

import com.nagarro.online_food_ordering_system.constant.Constant;
import com.nagarro.online_food_ordering_system.entity.Customer;
import com.nagarro.online_food_ordering_system.exception.UnauthorizedAccessException;
import com.nagarro.online_food_ordering_system.request.LoginRequest;
import com.nagarro.online_food_ordering_system.service.AuthService;
import com.nagarro.online_food_ordering_system.service.CustomerService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

	private final CustomerService customerService;

	public AuthServiceImpl(CustomerService customerService) {
		this.customerService = customerService;

	}

	@Override
	public String authenticateUser(LoginRequest loginRequest) {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		Customer existingCustomer = customerService.getCustomerByEmail(loginRequest.getEmail());
		if (bCryptPasswordEncoder.matches(loginRequest.getPassword(), existingCustomer.getPassword())) {
			return generateJWTToken(loginRequest);
		}
		throw new UnauthorizedAccessException("Incorrect Credentials", HttpStatus.UNAUTHORIZED.value());
	}

	private String generateJWTToken(LoginRequest loginRequest) {
		Customer customerInformation = customerService.getCustomerByEmail(loginRequest.getEmail());
		Long userId = customerInformation.getUserId();
		Date now = new Date();
		Date expirationDate = new Date(now.getTime() + Constant.ONE_HOUR);
		return Jwts.builder().setSubject(loginRequest.getEmail()).claim("userId", userId)
				.claim("role", customerInformation.getRole().toString()).setIssuedAt(now).setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS256, Constant.SECRET_KEY).compact();
	}
}
