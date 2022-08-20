package com.example.elevenstreet.acceptance;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;

import com.example.elevenstreet.common.Address;
import com.example.elevenstreet.order.dto.request.OrderCancelRequest;
import com.example.elevenstreet.order.dto.request.OrderRequest;
import com.example.elevenstreet.order.dto.request.SingleOrderRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class OrderAcceptanceTest {

	@LocalServerPort
	int port;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	@Test
	@DisplayName("주문 내역을 조회 기간으로 조회할 때 조건에 맞는 주문이 있으면 주문 페이징 목록이 반환된다.")
	void findOrdersByValidDate() {
		given()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.header("x-user-id", "greatpeople")

		.when()
			.get("/api/orders?start_date=2022-06-20T00:00&end_date=2022-08-21T00:00")

		.then()
			.statusCode(HttpStatus.OK.value())
			.body("first", equalTo(true))
			.body("size", equalTo(5));
	}

	@Test
	@DisplayName("주문 내역을 조회 기간으로 조회할 때 조건에 맞는 주문이 없으면 빈 주문 페이징 목록이 반환된다.")
	void findOrdersByInvalidDate() {
		given()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.header("x-user-id", "greatpeople")

			.when()
		.get("/api/orders?start_date=9999-12-31T00:00&end_date=9999-12-31T00:01")

		.then()
			.statusCode(HttpStatus.OK.value())
			.body("first", equalTo(true))
			.body("last", equalTo(true))
			.body("size", equalTo(5));
	}

	@Test
	@DisplayName("회원의 주문 요청이 정상적인 경우라면 Created 상태 코드와 생성한 주문의 id가 반환된다.")
	void createOrder() {
		OrderRequest orderRequest = createOrderRequest();

		given()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.header("x-user-id", "greatpeople")
			.contentType(ContentType.JSON)
			.body(orderRequest)

		.when()
			.post("/api/orders")

		.then()
			.statusCode(HttpStatus.CREATED.value())
			.body("orderId", notNullValue());
	}

	@Test
	@DisplayName("회원의 주문 취소 요청이 정상적인 경우라면 OK 상태 코드와 취소된 주문의 id가 반환된다.")
	void cancelOrder() {
		OrderCancelRequest orderCancelRequest = OrderCancelRequest.builder()
			.cancelPrice(4040000)
			.build();

		given()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.contentType(ContentType.JSON)
			.body(orderCancelRequest)

		.when()
			.post("/api/orders/1/cancel")

		.then()
			.statusCode(HttpStatus.OK.value())
			.body("orderId", notNullValue());
	}

	private OrderRequest createOrderRequest() {
		SingleOrderRequest singleOrderRequest = SingleOrderRequest.builder()
			.productId(1L)
			.price(3400000)
			.quantity(1)
			.build();

		Address address = Address.builder()
			.city("city")
			.street("street")
			.zipCode("zipCode")
			.build();

		return OrderRequest.builder()
			.address(address)
			.orders(List.of(singleOrderRequest))
			.build();
	}
}
