package com.example.elevenstreet.acceptance;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

import io.restassured.RestAssured;
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
}
