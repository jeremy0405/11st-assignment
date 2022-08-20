package com.example.elevenstreet.order.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class OrderCancelRequest {

	private Integer cancelPrice;
}
