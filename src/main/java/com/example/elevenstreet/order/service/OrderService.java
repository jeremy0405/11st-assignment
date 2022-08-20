package com.example.elevenstreet.order.service;

import com.example.elevenstreet.order.dto.request.OrderRequest;
import com.example.elevenstreet.order.dto.response.OrderResponse;

public interface OrderService {

	OrderResponse order(String userId, OrderRequest orderRequest);
}
