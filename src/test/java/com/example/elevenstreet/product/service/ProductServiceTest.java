package com.example.elevenstreet.product.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.elevenstreet.product.Product;
import com.example.elevenstreet.product.dto.ProductResponse;
import com.example.elevenstreet.product.repository.ProductRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductServiceTest {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@Test
	@DisplayName("displayDate 기준으로 전시중인 상품을 반환한다.")
	void getProductsByDisplayDate() {
	    //given
		LocalDateTime displayDate = LocalDateTime.of(2022, 10, 1, 0, 0);

		List<Product> products = productRepository.findAll();
		List<ProductResponse> actualProductResponse = products.stream()
			.filter(product -> product.getStartsAt().isBefore(displayDate) &&
				product.getEndsAt().isAfter(displayDate))
			.map(ProductResponse::from)
			.collect(Collectors.toList());

		Pageable pageable = PageRequest.of(0, products.size());

		//when
		Page<ProductResponse> productResponsePage = productService.getProducts(displayDate, pageable);
		List<ProductResponse> results = productResponsePage.stream()
			.collect(Collectors.toList());

		//then
		assertThat(results).usingRecursiveComparison()
			.isEqualTo(actualProductResponse);
	}

	@Test
	@DisplayName("displayDate에 해당하는 상품이 없다면 빈 content를 반환한다.")
	void getProductsByInvalidDisplayDate() {
	    //given
		LocalDateTime displayDate = LocalDateTime.of(9999, 12, 31, 0, 0);
		Pageable pageable = PageRequest.of(0, 10);

	    //when
		Page<ProductResponse> result = productService.getProducts(displayDate, pageable);

	    //then
		assertThat(result).isEmpty();
	}

}
