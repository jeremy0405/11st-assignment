package com.example.elevenstreet.product;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private Integer price;

	private Integer quantity;

	@Enumerated(EnumType.STRING)
	private ProductStatus status;

	private LocalDateTime startsAt;

	private LocalDateTime endsAt;

	@JoinColumn
	@ManyToOne(fetch = FetchType.LAZY)
	private Seller seller;

	public Long getSellerId() {
		return seller.getId();
	}

	public String getSellerName() {
		return seller.getName();
	}
}
