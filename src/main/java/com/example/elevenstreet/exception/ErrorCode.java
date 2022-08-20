package com.example.elevenstreet.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ErrorCode {

	NO_SUCH_PRODUCT("A001", "해당 상품이 존재하지 않습니다", 400),
	NO_SUCH_ORDER("A002", "해당 주문이 존재하지 않습니다", 400),

	NOT_ENOUGH_QUANTITY("B001", "재고가 부족합니다", 400),
	NOT_ENOUGH_INPUT_PRICE("B002", "입금된 금액이 충분하지 않습니다", 400),
	SUSPENDED_PRODUCT("B003", "판매 중지된 상품입니다", 400),

	NOT_AVAILABLE_CANCEL("C001", "이미 완료된 주문은 취소가 불가능합니다", 400),
	ALREADY_CANCELED("C002", "이미 취소된 주문은 취소가 불가능합니다", 400),
	NOT_MATCH_WITH_CANCELPRICE("C003", "취소 금액과 총 주문 금액이 일치하지 않습니다.", 400),

	INVALID_INPUT_VALUE("D001", "요청이 올바르지 않습니다", 400);

	private final String code;
	private final String message;
	private final int status;
}
