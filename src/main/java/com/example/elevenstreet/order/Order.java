package com.example.elevenstreet.order;

import com.example.elevenstreet.common.Address;
import com.example.elevenstreet.common.Timestamped;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
@Getter
@Setter(AccessLevel.PRIVATE)
public class Order extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "x_user_id")
	private String userId;

	@Embedded
	private Address address;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderProduct> orderProducts = new ArrayList<>();

	public static Order createOrder(String userId, Address address, List<OrderProduct> orderProducts) {
		Order order = new Order();
		order.setUserId(userId);
		order.setAddress(address);
		for (OrderProduct orderProduct : orderProducts) {
			order.addOrderProduct(orderProduct);
		}
		order.setStatus(OrderStatus.IN_PROGRESS);

		return order;
	}

	private void addOrderProduct(OrderProduct orderProduct) {
		this.orderProducts.add(orderProduct);
		orderProduct.assignOrder(this);
	}
}
