package com.example.elevenstreet.order.service;

import com.example.elevenstreet.exception.ErrorCode;
import com.example.elevenstreet.exception.NoSuchEntityException;
import com.example.elevenstreet.order.Order;
import com.example.elevenstreet.order.OrderProduct;
import com.example.elevenstreet.order.dto.request.OrderRequest;
import com.example.elevenstreet.order.dto.request.SingleOrderRequest;
import com.example.elevenstreet.order.dto.response.OrderResponse;
import com.example.elevenstreet.order.repository.OrderRepository;
import com.example.elevenstreet.product.Product;
import com.example.elevenstreet.product.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;

	@Transactional
	@Override
	public OrderResponse order(String userId, OrderRequest orderRequest) {

		List<OrderProduct> orderProducts = new ArrayList<>();

		for (SingleOrderRequest singleOrder : orderRequest.getOrders()) {
			Product product = productRepository.findById(singleOrder.getProductId())
				.orElseThrow(() -> new NoSuchEntityException(ErrorCode.NO_SUCH_PRODUCT));

			orderProducts.add(OrderProduct.createOrderProduct(product, singleOrder.getPrice(), singleOrder.getQuantity()));
		}

		Order order = Order.createOrder(userId, orderRequest.getAddress(), orderProducts);

		orderRepository.save(order);

		return OrderResponse.from(order.getId());
	}
}
