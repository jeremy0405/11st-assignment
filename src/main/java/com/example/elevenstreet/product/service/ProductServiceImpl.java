package com.example.elevenstreet.product.service;

import com.example.elevenstreet.product.Product;
import com.example.elevenstreet.product.dto.ProductResponse;
import com.example.elevenstreet.product.repository.ProductRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	@Cacheable(cacheNames = "getProducts", key = "#displayDate.toLocalDate().toString().concat(#pageable.pageNumber)")
	@Override
	public Page<ProductResponse> getProducts(LocalDateTime displayDate, Pageable pageable) {
		Page<Product> products = productRepository.findAll(pageable, displayDate);

		return products.map(ProductResponse::from);
	}
}
