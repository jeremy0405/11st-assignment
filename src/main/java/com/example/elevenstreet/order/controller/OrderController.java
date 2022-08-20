package com.example.elevenstreet.order.controller;

import com.example.elevenstreet.order.dto.request.OrderRequest;
import com.example.elevenstreet.order.dto.response.OrderHistoryResponse;
import com.example.elevenstreet.order.dto.response.OrderResponse;
import com.example.elevenstreet.order.service.OrderService;
import java.time.LocalDateTime;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

	private static final String USER_ID = "x-user-id";

	private final OrderService orderService;

	@GetMapping
	public Page<OrderHistoryResponse> getOrderHistory(@RequestHeader(USER_ID) String userId,
		@RequestParam("start_date") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime startDate,
		@RequestParam("end_date") @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime endDate,
		@PageableDefault(size = 5) Pageable pageable) {

		return orderService.getOrderHistory(userId, startDate, endDate, pageable);
	}

	@PostMapping
	public ResponseEntity<OrderResponse> order(@RequestHeader(USER_ID) String userId,
		@RequestBody @Valid OrderRequest orderRequest) {

		OrderResponse response = orderService.order(userId, orderRequest);

		return ResponseEntity.ok(response);
	}
}
