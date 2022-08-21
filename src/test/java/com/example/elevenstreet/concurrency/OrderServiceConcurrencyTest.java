package com.example.elevenstreet.concurrency;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.elevenstreet.common.Address;
import com.example.elevenstreet.order.dto.request.OrderRequest;
import com.example.elevenstreet.order.dto.request.SingleOrderRequest;
import com.example.elevenstreet.order.service.OrderService;
import com.example.elevenstreet.product.Product;
import com.example.elevenstreet.product.repository.ProductRepository;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceConcurrencyTest {

	@Autowired
	private OrderService orderService;

	@Autowired
	private ProductRepository productRepository;

	@Test
	@DisplayName("11번 상품에 대해 동시에 1개의 상품을 사는 100번의 주문을 한다면 동시성 제어를 통해 11번 상품의 재고가 100에서 0이 된다.")
	void orderConcurrency() throws InterruptedException {
		//given
		SingleOrderRequest singleOrderRequest = SingleOrderRequest.builder()
			.productId(11L)
			.price(10000)
			.quantity(1)
			.build();

		Address address = Address.builder()
			.city("city")
			.street("street")
			.zipCode("zipCode")
			.build();

		OrderRequest orderRequest = OrderRequest.builder()
			.address(address)
			.orders(List.of(singleOrderRequest))
			.build();

		int threadCount = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch countDownLatch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					orderService.order("greatpeople", orderRequest);
				} finally {
					countDownLatch.countDown();
				}
			});
		}

		countDownLatch.await();

		//when
		Product product = productRepository.findById(11L).get();

		//then
		assertThat(product.getQuantity()).isZero();
	}
}
