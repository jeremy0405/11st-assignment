package com.example.elevenstreet.order.dto.response;

import com.example.elevenstreet.order.OrderProduct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class OrderSingleHistoryResponse {

	private final String productName;

	private final Integer productPrice;

	private final Integer orderPrice;

	private final Integer orderQuantity;

	public static OrderSingleHistoryResponse from(OrderProduct orderProduct) {
		return new OrderSingleHistoryResponse(
			orderProduct.getProduct().getName(),
			orderProduct.getProduct().getPrice(),
			orderProduct.getTotalPrice(),
			orderProduct.getQuantity());
	}
}
