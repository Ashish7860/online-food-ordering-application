package com.nagarro.online_food_ordering_system.service.impl;

import com.nagarro.online_food_ordering_system.entity.Customer;
import com.nagarro.online_food_ordering_system.entity.Role;
import com.nagarro.online_food_ordering_system.exception.EmptyInputException;
import com.nagarro.online_food_ordering_system.exception.RecordAlreadyExistsException;
import com.nagarro.online_food_ordering_system.exception.RecordNotFoundException;
import com.nagarro.online_food_ordering_system.repository.CustomerRepository;
import com.nagarro.online_food_ordering_system.repository.DishRepository;
import com.nagarro.online_food_ordering_system.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerDao;
    private final DishRepository dishRepository;
    
    public CustomerServiceImpl(CustomerRepository customerDao, DishRepository dishRepository) {
        this.customerDao = customerDao;
        this.dishRepository = dishRepository;
    }

    @Override
    public Customer addCustomer(Customer customer) {
        
        if(customer.getEmail().isBlank() || customer.getFirstName().isBlank() || customer.getLastName().isBlank() || customer.getPassword().isBlank()){
            throw new EmptyInputException("Input cannot be null!!", HttpStatus.BAD_REQUEST.value());
        }

        // Checking if the user with the given email already exists or not
        Optional<Customer> optionalCustomer = customerDao.findByEmail(customer.getEmail());
        if(optionalCustomer.isPresent()){
            throw new RecordAlreadyExistsException("Customer already exists with email: " + customer.getEmail(), HttpStatus.BAD_REQUEST.value());
        }

        customer.setPassword(new BCryptPasswordEncoder().encode(customer.getPassword()));
        customer.setRole(Role.USER);
        return customerDao.save(customer);
    }

    @Override
    public Customer addAdmin(Customer customer) {
    	
        
        if(customer.getEmail().isBlank() || customer.getFirstName().isBlank() || customer.getLastName().isBlank() || customer.getPassword().isBlank()){
            throw new EmptyInputException("Input cannot be null!!", HttpStatus.BAD_REQUEST.value());
        }

        // Checking if the user with the given email already exists or not
        Optional<Customer> optionalCustomer = customerDao.findByEmail(customer.getEmail());
        if(optionalCustomer.isPresent()){
            throw new RecordAlreadyExistsException("Admin already exists with email: " + customer.getEmail(), HttpStatus.BAD_REQUEST.value());
        }

        
        customer.setPassword(new BCryptPasswordEncoder().encode(customer.getPassword()));
        customer.setRole(Role.ADMIN);
        return customerDao.save(customer);
    }

    @Override
    public Customer getCustomerByUsername(String username) {
        return customerDao.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("Customer not found with username: " + username, HttpStatus.NOT_FOUND.value()));
    }
    
    @Override
    public void seedAdmin(Customer customer) {
        customer.setPassword(new BCryptPasswordEncoder().encode(customer.getPassword()));
        customerDao.save(customer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerDao.findAll();
    }

    @Override
    public Customer getCustomerById(Long customerId) {
        // Checking if the user with the given userId already exists or not
        Optional<Customer> optionalCustomer = customerDao.findById(customerId);
        if(optionalCustomer.isEmpty()){
            throw new RecordNotFoundException("Customer not found with id: " + customerId, HttpStatus.NOT_FOUND.value());
        }
        return optionalCustomer.get();
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        Optional<Customer> optionalUser = customerDao.findByEmail(email);
        if(optionalUser.isEmpty()){
            throw new RecordNotFoundException("User not found with email: " + email, HttpStatus.NOT_FOUND.value());
        }
        return optionalUser.get();
    }
    
    //Updating the details of User
    @Override
    public Customer updateCustomer(Long customerId, Customer customer) {
        System.out.println("Updating customer with ID: " + customerId);
        Customer existingCustomer = customerDao.findById(customerId)
                .orElseThrow(() -> new RecordNotFoundException("Customer not found with id: " + customerId, HttpStatus.NOT_FOUND.value()));
        
        
        // Only update allowed fields
        existingCustomer.setFirstName(customer.getFirstName());
        existingCustomer.setLastName(customer.getLastName());
        return customerDao.save(existingCustomer);
    }
    
    @Override
    public void deleteCustomer(Long customerId) {
    	
        // Checking if the user with the given id already exists or not
        Optional<Customer> existingCustomer = customerDao.findById(customerId);
        if(existingCustomer.isEmpty()){
            throw new RecordNotFoundException("Customer not found with id: " + customerId, HttpStatus.NOT_FOUND.value());
        }
        customerDao.deleteById(customerId);
    }
}
