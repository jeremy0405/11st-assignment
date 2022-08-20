package com.example.elevenstreet.order.dto.request;

import com.example.elevenstreet.common.Address;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class OrderRequest {

	@NotNull
	@Valid
	private List<SingleOrderRequest> orders;

	@NotNull
	@Valid
	private Address address;
}
