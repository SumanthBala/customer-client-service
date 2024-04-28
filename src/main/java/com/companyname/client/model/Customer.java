package com.companyname.client.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

	private String firstName;
	private String lastName;
	private LocalDate dateOfBirth;
}
