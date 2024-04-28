package com.companyname.client.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.companyname.client.model.Customer;
import com.companyname.client.service.CustomerClientService;

@ContextConfiguration(classes = { CustomerClientControllerTest.class })
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class CustomerClientControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private CustomerClientController customerClientController;

	@Mock
	private CustomerClientService customerClientService;

	@BeforeEach
	public void setUp() {

		mockMvc = MockMvcBuilders.standaloneSetup(customerClientController).build();
	}

	private List<Customer> getMockCustomers() {

		List<Customer> customerList = new ArrayList<>();
		customerList.add(Customer.builder().firstName("Arun").lastName("Garimella")
				.dateOfBirth(LocalDate.of(1992, 06, 14)).build());
		return customerList;
	}

	@Test
	public void testGetCustomers() throws Exception {

		HttpHeaders headers = new HttpHeaders();

		when(customerClientService.getAllCustomers()).thenReturn(getMockCustomers());
		mockMvc.perform(MockMvcRequestBuilders.get("/client/customers/").contentType(MediaType.APPLICATION_JSON)
				.headers(headers)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$[0].firstName").value("Arun"))
				.andExpect(jsonPath("$[0].lastName").value("Garimella"));

	}

	@Test
	public void testGetCustomerByLastName() throws Exception {
		when(customerClientService.getCustomerByLastName(any())).thenReturn(getMockCustomers());
		mockMvc.perform(get("/client/customers/search").param("lastName", "Garimella"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$[0].firstName").value("Arun"))
				.andExpect(jsonPath("$[0].lastName").value("Garimella"));

	}

	@Test
	public void testAddCustomer() throws Exception {

		when(customerClientService.createCustomer(any())).thenReturn(Customer.builder().firstName("Arun")
				.lastName("Garimella").dateOfBirth(LocalDate.of(1992, 06, 14)).build());

		mockMvc.perform(post("/client/customers/").contentType(MediaType.APPLICATION_JSON)
				.content("{\r\n" + "\"firstName\":\"Arun\",\r\n" + "\"lastName\":\"Garimella\",\r\n"
						+ "\"dateOfBirth\":\"1992-06-14\"\r\n" + "}"))
				.andExpect(status().isOk()).andExpect(jsonPath("$.firstName").value("Arun"));
	}
}
