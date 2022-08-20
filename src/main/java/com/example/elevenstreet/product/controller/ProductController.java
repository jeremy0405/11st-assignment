package com.example.elevenstreet.product.controller;

import com.example.elevenstreet.product.dto.ProductResponse;
import com.example.elevenstreet.product.service.ProductService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@GetMapping
	public Page<ProductResponse> getProducts(
		@RequestParam(name = "display_date", required = false) @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime displayDate,
		@PageableDefault(size = 5) Pageable pageable) {

		if (displayDate == null) {
			displayDate = LocalDateTime.now();
		}

		return productService.getProducts(displayDate, pageable);
	}
}
