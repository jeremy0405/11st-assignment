package com.example.elevenstreet.product.dto;

import com.example.elevenstreet.product.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ProductResponse {

	private final Long id;

	private final String name;

	private final Integer price;

	private final Integer quantity;

	private final Long sellerId;

	private final String sellerName;

	private final String status;

	public static ProductResponse from(Product product) {
		return new ProductResponse(
			product.getId(),
			product.getName(),
			product.getPrice(),
			product.getQuantity(),
			product.getSellerId(),
			product.getSellerName(),
			product.getStatus().name());
	}
}
