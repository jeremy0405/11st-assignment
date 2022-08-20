package com.example.elevenstreet.order.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.elevenstreet.order.Order;
import com.example.elevenstreet.order.dto.response.OrderHistoryResponse;
import com.example.elevenstreet.order.repository.OrderRepository;
import java.time.LocalDateTime;
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

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderRepository orderRepository;

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

		Page<OrderHistoryResponse> actual =
			new PageImpl<>(actualOrderHistories, pageable, actualOrderHistories.size());

		//when
		Page<OrderHistoryResponse> result =
			orderService.getOrderHistory(USER_ID, startDate, endDate, pageable);

		//then
		assertThat(actual)
			.usingRecursiveComparison()
			.isEqualTo(result);
	}

	@Test
	@DisplayName("")
	void OrderServiceTest() {
	    //given

	    //when

	    //then

	}
}
