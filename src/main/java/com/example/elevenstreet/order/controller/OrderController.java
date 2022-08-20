package com.example.elevenstreet.order.controller;

import com.example.elevenstreet.order.dto.request.OrderRequest;
import com.example.elevenstreet.order.dto.response.OrderResponse;
import com.example.elevenstreet.order.service.OrderService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

	private static final String USER_ID = "x-user-id";

	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<OrderResponse> order(@RequestHeader(USER_ID) String userId,
		@RequestBody @Valid OrderRequest orderRequest) {

		OrderResponse response = orderService.order(userId, orderRequest);

		return ResponseEntity.ok(response);
	}
}
