package com.example.elevenstreet.common;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;

@Embeddable
@Getter
public class Address {

	@NotEmpty
	private String city;

	@NotEmpty
	private String street;

	@NotEmpty
	private String zipCode;
}
