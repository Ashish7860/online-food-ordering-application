package com.nagarro.online_food_ordering_system.controller;

import com.nagarro.online_food_ordering_system.entity.Customer;
import com.nagarro.online_food_ordering_system.entity.Role;
import com.nagarro.online_food_ordering_system.exception.RecordNotFoundException;
import com.nagarro.online_food_ordering_system.service.CustomerService;
import com.nagarro.online_food_ordering_system.service.DishReviewService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/users")
public class CustomerController {

    private final CustomerService customerService;
    private final DishReviewService dishReviewService;

    public CustomerController(CustomerService customerService, DishReviewService dishReviewService) {
        this.customerService = customerService;
        this.dishReviewService = dishReviewService;
    }

    @PostMapping("/new")
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer) {
        Customer newUser = customerService.addCustomer(customer);
        return new ResponseEntity<>(newUser, new HttpHeaders(), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/admin/new")
    public ResponseEntity<Customer> addAdmin(@RequestBody Customer customer) {
        Customer newAdmin = customerService.addAdmin(customer);
        return new ResponseEntity<>(newAdmin, new HttpHeaders(), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #id == principal.id)")
    @GetMapping("/customerId/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("id") Long id) {
        Customer fetchedCustomer = customerService.getCustomerById(id);
        return new ResponseEntity<>(fetchedCustomer, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Customer> getCustomerByEmail(@PathVariable("email") String email) {
        Customer fetchedCustomer = customerService.getCustomerByEmail(email);
        return new ResponseEntity<>(fetchedCustomer, new HttpHeaders(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return new ResponseEntity<>(customers, new HttpHeaders(), HttpStatus.OK);
    }

    @PutMapping("/customerId/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") String idString, @RequestBody Customer customer) {
        
    	// Trim the id string to remove any whitespace or newline characters
        Long id = Long.parseLong(idString.trim());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof Customer) {
            Customer principal = (Customer) auth.getPrincipal();

            if (!principal.getUserId().equals(id) && principal.getRole() != Role.ADMIN) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            Customer updatedCustomer = customerService.updateCustomer(id, customer);
            return new ResponseEntity<>(updatedCustomer, new HttpHeaders(), HttpStatus.OK);
        } catch (RecordNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    //To delete the User
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("id") Long id) {
        customerService.deleteCustomer(id);
        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.NO_CONTENT);
    }
}