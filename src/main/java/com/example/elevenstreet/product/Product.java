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
import javax.persistence.Version;
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

	public void reduceQuantity(Integer quantity) {
		if (this.quantity < quantity) {
			throw new RuntimeException("재고가 부족합니다");
		}
		this.quantity -= quantity;
	}

	public void checkProductStatus() {
		if (status == ProductStatus.SUSPENDED) {
			throw new RuntimeException("판매 중지된 상품입니다");
		}
	}

	public void checkTotalPrice(Integer inputPrice, Integer quantity) {
		if (this.price * quantity > inputPrice) {
			throw new RuntimeException("입금된 금액이 충분하지 않습니다");
		}
	}

	public Long getSellerId() {
		return seller.getId();
	}

	public String getSellerName() {
		return seller.getName();
	}
}
