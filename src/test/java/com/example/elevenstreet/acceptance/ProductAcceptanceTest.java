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
class ProductAcceptanceTest {

	@LocalServerPort
	int port;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	@Test
	@DisplayName("상품 조회를 할 때 display_date에 해당하는 상품이 있으면 상품 페이징 목록이 반환된다.")
	void findProducts() {
		given()
			.accept(MediaType.APPLICATION_JSON_VALUE)

		.when()
			.get("/api/products?display_date=2022-08-20T00:00")

		.then()
			.statusCode(HttpStatus.OK.value())
			.body("first", equalTo(true))
			.body("size", equalTo(5));
	}

	@Test
	@DisplayName("상품 조회를 할 때 display_date에 해당하는 상품이 없으면 빈 상품 페이징 목록이 반환된다.")
	void findProductsWithInvalidDisplayDate() {
		given()
			.accept(MediaType.APPLICATION_JSON_VALUE)

		.when()
			.get("/api/products?display_date=9999-12-31T00:00")

		.then()
			.statusCode(HttpStatus.OK.value())
			.body("first", equalTo(true))
			.body("last", equalTo(true))
			.body("size", equalTo(5));
	}
}
