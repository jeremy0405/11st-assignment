package com.example.elevenstreet.order.dto.response;

import com.example.elevenstreet.common.Address;
import com.example.elevenstreet.order.Order;
import com.example.elevenstreet.order.OrderProduct;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class OrderHistoryResponse {

	private final Long orderId;

	private final List<OrderSingleHistoryResponse> orderHistories;

	private final Address address;

	public static OrderHistoryResponse from(Order order) {
		List<OrderSingleHistoryResponse> orderHistories = new ArrayList<>();
		for (OrderProduct orderProduct : order.getOrderProducts()) {
			orderHistories.add(OrderSingleHistoryResponse.from(orderProduct));
		}
		return new OrderHistoryResponse(order.getId(), orderHistories, order.getAddress());
	}
}
