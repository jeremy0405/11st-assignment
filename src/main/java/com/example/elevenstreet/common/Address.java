package com.example.elevenstreet.common;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Address {

	@NotEmpty
	private String city;

	@NotEmpty
	private String street;

	@NotEmpty
	private String zipCode;
}
