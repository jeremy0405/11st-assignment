package com.example.elevenstreet.order.dto.request;

import com.example.elevenstreet.common.Address;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
