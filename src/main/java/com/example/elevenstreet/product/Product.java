package com.example.elevenstreet.product;

import com.example.elevenstreet.exception.ErrorCode;
import com.example.elevenstreet.exception.ProductException;
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

	public void reduceQuantity(Integer quantity) {
		if (this.quantity < quantity) {
			throw new ProductException(ErrorCode.NOT_ENOUGH_QUANTITY);
		}
		this.quantity -= quantity;
	}

	public void checkProductStatus() {
		if (status == ProductStatus.SUSPENDED) {
			throw new ProductException(ErrorCode.SUSPENDED_PRODUCT);
		}
	}

	public void checkTotalPrice(Integer inputPrice, Integer quantity) {
		if (calculateTotalPrice(quantity) > inputPrice) {
			throw new ProductException(ErrorCode.NOT_ENOUGH_INPUT_PRICE);
		}
	}

	private int calculateTotalPrice(Integer quantity) {
		return this.price * quantity;
	}

	public void increaseQuantity(Integer quantity) {
		this.quantity += quantity;
	}

	public Long getSellerId() {
		return seller.getId();
	}

	public String getSellerName() {
		return seller.getName();
	}
}
