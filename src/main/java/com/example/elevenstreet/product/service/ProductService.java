package com.example.elevenstreet.product.service;

import com.example.elevenstreet.product.dto.ProductResponse;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

	Page<ProductResponse> getProducts(LocalDateTime displayDate, Pageable pageable);
}
