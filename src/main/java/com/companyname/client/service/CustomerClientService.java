package com.companyname.client.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.companyname.client.model.Customer;

import jakarta.annotation.PostConstruct;

@Service
public class CustomerClientService {

	@Value("${customer.service.base.url}")
	private String baseUrl;

	private static final String CUSTOMER_SERVICE_PATH = "/v1/api/customers/";

	private RestTemplate restTemplate = new RestTemplate();

	private HttpHeaders headers;

	@Autowired
	private AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientServiceAndManager;

	@PostConstruct
	public void createHttpHeaders() {
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
	}

	public OAuth2AccessToken getAccessToken() {
		OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId("okta")
				.principal("Demo Service").build();

		// Perform the actual authorization request using the authorized client service
		// and authorized client
		// manager. This is where the JWT is retrieved from the Okta servers.
		OAuth2AuthorizedClient authorizedClient = this.authorizedClientServiceAndManager.authorize(authorizeRequest);

		// Get the token from the authorized client object
		return Objects.requireNonNull(authorizedClient).getAccessToken();
	}

	public List<Customer> getAllCustomers() {

		// Add the JWT to the RestTemplate headers

		headers.add("Authorization", "Bearer " + getAccessToken().getTokenValue());
		HttpEntity<String> entity = new HttpEntity<>(headers);

		
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<Customer[]> response = restTemplate.exchange(baseUrl + CUSTOMER_SERVICE_PATH, HttpMethod.GET,
				entity, Customer[].class);

		return Arrays.asList(response.getBody());
	}

	public Customer createCustomer(Customer customer) {
		// Add the JWT to the RestTemplate headers

		headers.add("Authorization", "Bearer " + getAccessToken().getTokenValue());
		HttpEntity<Customer> entity = new HttpEntity<>(customer, headers);

		ResponseEntity<Customer> response = restTemplate.exchange(baseUrl + CUSTOMER_SERVICE_PATH, HttpMethod.POST,
				entity, Customer.class);

		return response.getBody();
	}

	public List<Customer> getCustomerByLastName(String lastName) {
		headers.add("Authorization", "Bearer " + getAccessToken().getTokenValue());
		HttpEntity<String> entity = new HttpEntity<>(headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + CUSTOMER_SERVICE_PATH + "search")
				.queryParam("lastName", lastName);

		ResponseEntity<Customer[]> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
				Customer[].class);

		return Arrays.asList(response.getBody());
	}
}
