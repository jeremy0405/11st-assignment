package com.example.elevenstreet.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.elevenstreet.common.Address;
import com.example.elevenstreet.exception.ErrorCode;
import com.example.elevenstreet.exception.NoSuchEntityException;
import com.example.elevenstreet.exception.OrderException;
import com.example.elevenstreet.exception.ProductException;
import com.example.elevenstreet.order.Order;
import com.example.elevenstreet.order.OrderProduct;
import com.example.elevenstreet.order.OrderStatus;
import com.example.elevenstreet.order.dto.request.OrderCancelRequest;
import com.example.elevenstreet.order.dto.request.OrderRequest;
import com.example.elevenstreet.order.dto.request.SingleOrderRequest;
import com.example.elevenstreet.order.dto.response.OrderHistoryResponse;
import com.example.elevenstreet.order.dto.response.OrderResponse;
import com.example.elevenstreet.order.repository.OrderRepository;
import com.example.elevenstreet.product.Product;
import com.example.elevenstreet.product.repository.ProductRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

	private static final String USER_ID = "greatpeople";
	private static final String CITY = "서울 송파구";
	private static final String STREET = "송파대로 567";
	private static final String ZIP_CODE = "05503";

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@Test
	@DisplayName("회원이 주문을 조회하면 회원 ID와 startDate, endDate기준으로 주문이 페이징 하여 반환한다.")
	void getOrderHistoryByUserIdAndDate() {
	    //given
		LocalDateTime startDate = LocalDateTime.of(2022, 6, 1, 0, 0);
		LocalDateTime endDate = LocalDateTime.of(2022, 12, 31, 0, 0);

		List<Order> orders = orderRepository.findAll();
		List<OrderHistoryResponse> actualOrderHistories = orders.stream()
			.filter(order -> order.getUserId().equals(USER_ID) &&
				order.getCreatedAt().isAfter(startDate) &&
				order.getCreatedAt().isBefore(endDate))
			.map(OrderHistoryResponse::from)
			.collect(Collectors.toList());

		Pageable pageable = PageRequest.of(0, orders.size());

		Page<OrderHistoryResponse> orderHistoryResponses =
			new PageImpl<>(actualOrderHistories, pageable, actualOrderHistories.size());

		//when
		Page<OrderHistoryResponse> result =
			orderService.getOrderHistory(USER_ID, startDate, endDate, pageable);

		//then
		assertThat(result)
			.usingRecursiveComparison()
			.isEqualTo(orderHistoryResponses);
	}

	@Test
	@DisplayName("회원이 한 개의 상품에 대해 올바른 주문을 하면 상품의재고가 감소하고 주문 정보가 저장된 후 주문 식별자를 반환한다.")
	void createValidOneProductOrder() {
	    //given
		Long productId = 1L;
		Integer quantity = 2;
		Integer price = 6800000;

		SingleOrderRequest singleOrderRequest = SingleOrderRequest.builder()
			.productId(productId)
			.price(price)
			.quantity(quantity)
			.build();

		Address address = Address.builder()
			.city(CITY)
			.street(STREET)
			.zipCode(ZIP_CODE)
			.build();

		Product beforeUpdatedProduct = productRepository.findById(productId).get();
		Integer beforeUpdatedQuantity = beforeUpdatedProduct.getQuantity();

		OrderRequest orderRequest = OrderRequest.builder()
			.address(address)
			.orders(List.of(singleOrderRequest))
			.build();

		//when
		OrderResponse orderResponse = orderService.order(USER_ID, orderRequest);
		Long orderId = orderResponse.getOrderId();
		Order order = orderRepository.findById(orderId).get();

		//then
		assertThat(order.getAddress()).isEqualTo(address);
		assertThat(order.getUserId()).isEqualTo(USER_ID);
		assertThat(order.getStatus()).isEqualTo(OrderStatus.IN_PROGRESS);
		for (OrderProduct orderProduct : order.getOrderProducts()) {
			assertThat(orderProduct.getProduct().getId()).isEqualTo(productId);
			assertThat(orderProduct.getQuantity()).isEqualTo(quantity);
			assertThat(orderProduct.getTotalPrice()).isEqualTo(price);
			Product product = orderProduct.getProduct();
			assertThat(product.getQuantity()).isEqualTo(beforeUpdatedQuantity - quantity);
		}
	}

	@Test
	@DisplayName("회원이 여러 개의 상품에 대해 올바른 주문을 하면 상품의재고가 감소하고 주문 정보가 저장된 후 주문 식별자를 반환한다.")
	void createValidMultiProductOrder() {
		//given
		Long[] productId = new Long[]{1L, 2L};
		Integer[] quantity = new Integer[]{2, 1};
		Integer[] price = new Integer[]{6800000, 800000};
		Integer[] beforeUpdatedQuantity = new Integer[2];

		List<SingleOrderRequest> orders = new ArrayList<>();
		for (int i = 0; i < productId.length; i++) {
			orders.add(SingleOrderRequest.builder()
				.productId(productId[i])
				.price(price[i])
				.quantity(quantity[i])
				.build());
			Product beforeUpdatedProduct = productRepository.findById(productId[i]).get();
			beforeUpdatedQuantity[i] = beforeUpdatedProduct.getQuantity();
		}

		Address address = Address.builder()
			.city(CITY)
			.street(STREET)
			.zipCode(ZIP_CODE)
			.build();

		OrderRequest orderRequest = OrderRequest.builder()
			.address(address)
			.orders(orders)
			.build();

		//when
		OrderResponse orderResponse = orderService.order(USER_ID, orderRequest);
		Long orderId = orderResponse.getOrderId();
		Order order = orderRepository.findById(orderId).get();

		//then
		assertThat(order.getAddress()).isEqualTo(address);
		assertThat(order.getUserId()).isEqualTo(USER_ID);
		assertThat(order.getStatus()).isEqualTo(OrderStatus.IN_PROGRESS);
		List<OrderProduct> orderProducts = order.getOrderProducts();
		for (int i = 0; i < orderProducts.size(); i++) {
			assertThat(orderProducts.get(i).getProduct().getId()).isEqualTo(productId[i]);
			assertThat(orderProducts.get(i).getQuantity()).isEqualTo(quantity[i]);
			assertThat(orderProducts.get(i).getTotalPrice()).isEqualTo(price[i]);
			Product product = orderProducts.get(i).getProduct();
			assertThat(product.getQuantity()).isEqualTo(beforeUpdatedQuantity[i] - quantity[i]);
		}
	}

	@Test
	@DisplayName("회원의 주문에서 상품의 ID가 유효하지 않으면 예외를 반환한다.")
	void createInValidOrderByProductId() {
		//given
		OrderRequest orderRequest = createOrderRequest(9999999999L, 2, 100);

		//when

		//then
		assertThatThrownBy(() -> orderService.order(USER_ID, orderRequest))
			.isInstanceOf(NoSuchEntityException.class)
			.hasMessage(ErrorCode.NO_SUCH_PRODUCT.getMessage());
	}

	@Test
	@DisplayName("회원의 주문에서 가격이 불충분하다면 예외를 반환한다.")
	void createInValidOrderByNotEnoughInputPrice() {
		//given
		OrderRequest orderRequest = createOrderRequest(1L, 2, 100);

		//when

		//then
		assertThatThrownBy(() -> orderService.order(USER_ID, orderRequest))
			.isInstanceOf(ProductException.class)
			.hasMessage(ErrorCode.NOT_ENOUGH_INPUT_PRICE.getMessage());
	}

	@Test
	@DisplayName("회원의 주문에서 재고가 불충분하다면 예외를 반환한다.")
	void createInValidOrderByNotEnoughQuantity() {
		//given
		OrderRequest orderRequest = createOrderRequest(2L, 20, 1600000);

		//when

		//then
		assertThatThrownBy(() -> orderService.order(USER_ID, orderRequest))
			.isInstanceOf(ProductException.class)
			.hasMessage(ErrorCode.NOT_ENOUGH_QUANTITY.getMessage());
	}

	@Test
	@DisplayName("회원의 주문에서 판매 중지된 상품이 있다면 예외를 반환한다.")
	void createInValidOrderBySuspendedProduct() {
		//given
		OrderRequest orderRequest = createOrderRequest(9L, 1, 1000000);

		//when

		//then
		assertThatThrownBy(() -> orderService.order(USER_ID, orderRequest))
			.isInstanceOf(ProductException.class)
			.hasMessage(ErrorCode.SUSPENDED_PRODUCT.getMessage());
	}

	@Test
	@DisplayName("회원이 주문 취소를 한다면 주문의 상태가 CANCELED로 바뀌고 상품의 재고가 원래 값으로 늘어나며 주문 식별자 값을 반환한다.")
	void cancelValidOrder() {
		//given
		Long orderId = 2L;
		OrderCancelRequest orderCancelRequest = OrderCancelRequest.builder()
			.cancelPrice(500000)
			.build();

		Order beforeCanceledOrder = orderRepository.findById(orderId).get();
		OrderProduct beforeCanceledOrderProduct = beforeCanceledOrder.getOrderProducts().get(0);
		Integer quantity = beforeCanceledOrderProduct.getQuantity();
		Integer beforeCanceledQuantity = beforeCanceledOrderProduct.getProduct().getQuantity();

		//when
		OrderResponse orderResponse = orderService.cancelOrder(orderId, orderCancelRequest);
		Long responseOrderId = orderResponse.getOrderId();
		Order order = orderRepository.findById(responseOrderId).get();

		//then
		assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELED);
		assertThat(order.getOrderProducts().get(0).getProduct().getQuantity())
			.isEqualTo(beforeCanceledQuantity + quantity);
	}

	@Test
	@DisplayName("회원의 주문 취소에서 주문의 ID가 유효하지 않으면 예외를 반환한다.")
	void cancelInvalidOrderByOrderId() {
		//given
		OrderCancelRequest orderCancelRequest = OrderCancelRequest.builder()
			.cancelPrice(1000)
			.build();

		//when

		//then
		assertThatThrownBy(() -> orderService.cancelOrder(9999999999L, orderCancelRequest))
			.isInstanceOf(NoSuchEntityException.class)
			.hasMessage(ErrorCode.NO_SUCH_ORDER.getMessage());
	}

	@Test
	@DisplayName("회원의 주문 취소에서 취소 금액이 유효하지 않으면 예외를 반환한다.")
	void cancelInvalidOrderByCancelPrice() {
		//given
		OrderCancelRequest orderCancelRequest = OrderCancelRequest.builder()
			.cancelPrice(1)
			.build();

		//when

		//then
		assertThatThrownBy(() -> orderService.cancelOrder(1L, orderCancelRequest))
			.isInstanceOf(OrderException.class)
			.hasMessage(ErrorCode.NOT_MATCH_WITH_CANCELPRICE.getMessage());
	}

	@Test
	@DisplayName("회원의 주문 취소에서 주문이 배송 완료상태면 예외를 반환한다.")
	void cancelInvalidOrderByCompletedStatus() {
		//given
		OrderCancelRequest orderCancelRequest = OrderCancelRequest.builder()
			.cancelPrice(3400000)
			.build();

		//when

		//then
		assertThatThrownBy(() -> orderService.cancelOrder(6L, orderCancelRequest))
			.isInstanceOf(OrderException.class)
			.hasMessage(ErrorCode.NOT_AVAILABLE_CANCEL.getMessage());
	}

	@Test
	@DisplayName("회원의 주문 취소에서 주문이 이미 취소된 상태면 예외를 반환한다.")
	void cancelInvalidOrderByCanceledStatus() {
		//given
		OrderCancelRequest orderCancelRequest = OrderCancelRequest.builder()
			.cancelPrice(330000)
			.build();

		//when

		//then
		assertThatThrownBy(() -> orderService.cancelOrder(5L, orderCancelRequest))
			.isInstanceOf(OrderException.class)
			.hasMessage(ErrorCode.ALREADY_CANCELED.getMessage());
	}

	private OrderRequest createOrderRequest(Long productId, Integer quantity, Integer price) {
		SingleOrderRequest singleOrderRequest = SingleOrderRequest.builder()
			.productId(productId)
			.price(price)
			.quantity(quantity)
			.build();

		Address address = Address.builder()
			.city(CITY)
			.street(STREET)
			.zipCode(ZIP_CODE)
			.build();

		return OrderRequest.builder()
			.address(address)
			.orders(List.of(singleOrderRequest))
			.build();
	}
}
