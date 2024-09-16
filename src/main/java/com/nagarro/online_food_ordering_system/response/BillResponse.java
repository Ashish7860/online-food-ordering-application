package com.nagarro.online_food_ordering_system.response;

import com.nagarro.online_food_ordering_system.entity.Customer;
import com.nagarro.online_food_ordering_system.entity.Order;
import lombok.Builder;



@Builder
public class BillResponse {
    private Order order;
    private Customer customer;
	public BillResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public BillResponse(Order order, Customer customer) {
		super();
		this.order = order;
		this.customer = customer;
	}
}
