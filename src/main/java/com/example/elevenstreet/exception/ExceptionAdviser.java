package com.example.elevenstreet.exception;

import com.example.elevenstreet.exception.dto.ErrorDetailResponse;
import com.example.elevenstreet.exception.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ExceptionAdviser {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	private ResponseEntity<ErrorDetailResponse> methodArgumentNotValidExceptionHandler(
		MethodArgumentNotValidException e) {

		return ResponseEntity.badRequest().body(ErrorDetailResponse.of(e));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	private ResponseEntity<ErrorDetailResponse> methodArgumentTypeMismatchExceptionHandler(
		MethodArgumentTypeMismatchException e) {

		return ResponseEntity.badRequest().body(ErrorDetailResponse.of(e));
	}

	@ExceptionHandler(ApplicationException.class)
	private ResponseEntity<ErrorResponse> applicationExceptionHandler(ApplicationException e) {
		return ResponseEntity.status(e.getErrorCode().getStatus()).body(ErrorResponse.of(e.getErrorCode()));
	}
}
