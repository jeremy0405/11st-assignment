package com.example.elevenstreet.exception.dto;

import com.example.elevenstreet.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ErrorInfo {

	private final String code;
	private final String message;

	public static ErrorInfo of(ErrorCode errorCode) {
		return new ErrorInfo(errorCode.getCode(), errorCode.getMessage());
	}
}
