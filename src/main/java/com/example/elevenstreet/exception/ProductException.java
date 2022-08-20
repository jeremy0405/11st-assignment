package com.example.elevenstreet.exception;

public class ProductException extends ApplicationException {

	public ProductException(ErrorCode errorCode) {
		super(errorCode);
	}

	public ProductException(ErrorCode errorCode, Throwable cause) {
		super(errorCode, cause);
	}
}
