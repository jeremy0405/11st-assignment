package com.example.elevenstreet.order.dto.request;

import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SingleOrderRequest {

	@Positive
	private Long productId;

	@Positive
	private Integer price;

	@Positive
	private Integer quantity;
}
