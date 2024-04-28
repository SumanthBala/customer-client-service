package com.companyname.client.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.companyname.client.model.Customer;
import com.companyname.client.service.CustomerClientService;

@RestController
@RequestMapping("/client/customers")
public class CustomerClientController {

	@Autowired
	private CustomerClientService customerClientService;

	@GetMapping("/")
	public List<Customer> getAllCustomers() {
		List<Customer> customers = customerClientService.getAllCustomers();
		return customers;
	}

	@PostMapping("/")
	public Customer addCustomer(@RequestBody Customer customer) {
		return customerClientService.createCustomer(customer);
	}
	
	@GetMapping("/search")
	public List<Customer> getAllCustomersByLastName(@RequestParam("lastName") String lastName) {
		List<Customer> customers = customerClientService.getCustomerByLastName(lastName);
		return customers;
	}
}
