package com.example.elevenstreet.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ErrorCode {

	NO_SUCH_PRODUCT("A001", "해당 상품이 존재하지 않습니다", 400),

	NOT_ENOUGH_QUANTITY("B001", "재고가 부족합니다", 400),
	NOT_ENOUGH_INPUT_PRICE("B002", "입금된 금액이 충분하지 않습니다", 400),
	SUSPENDED_PRODUCT("B003", "판매 중지된 상품입니다", 400),

	INVALID_INPUT_VALUE("C001", "요청이 올바르지 않습니다", 400);

	private final String code;
	private final String message;
	private final int status;
}
