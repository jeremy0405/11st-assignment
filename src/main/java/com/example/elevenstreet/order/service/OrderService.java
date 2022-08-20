package com.example.elevenstreet.order.service;

import com.example.elevenstreet.order.dto.request.OrderRequest;
import com.example.elevenstreet.order.dto.response.OrderHistoryResponse;
import com.example.elevenstreet.order.dto.response.OrderResponse;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

	Page<OrderHistoryResponse> getOrderHistory(String userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

	OrderResponse order(String userId, OrderRequest orderRequest);
}
