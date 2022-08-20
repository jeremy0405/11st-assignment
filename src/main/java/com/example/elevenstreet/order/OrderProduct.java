package com.example.elevenstreet.order;

import com.example.elevenstreet.product.Product;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter(AccessLevel.PRIVATE)
public class OrderProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn
	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;

	@JoinColumn
	@ManyToOne(fetch = FetchType.LAZY)
	private Order order;

	private Integer totalPrice;

	private Integer quantity;

	public static OrderProduct createOrderProduct(Product product, Integer totalPrice, Integer quantity) {
		OrderProduct orderProduct = new OrderProduct();
		orderProduct.setProduct(product);
		orderProduct.setTotalPrice(totalPrice);
		orderProduct.setQuantity(quantity);

		product.reduceQuantity(quantity);
		product.checkTotalPrice(totalPrice, quantity);
		product.checkProductStatus();
		return orderProduct;
	}

	public void assignOrder(Order order) {
		this.order = order;
	}

	public void cancel() {
		getProduct().increaseQuantity(quantity);
	}
}
