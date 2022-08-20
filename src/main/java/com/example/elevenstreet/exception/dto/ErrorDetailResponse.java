package com.example.elevenstreet.exception.dto;

import com.example.elevenstreet.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ErrorDetailResponse {

	private final ErrorResponse errorResponse;

	private final List<RequestFieldError> errors;

	public static ErrorDetailResponse of(MethodArgumentNotValidException e) {
		List<RequestFieldError> errors = ErrorDetailResponse.RequestFieldError.of(e.getBindingResult());
		return new ErrorDetailResponse(ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE), errors);
	}

	public static ErrorDetailResponse of(MethodArgumentTypeMismatchException e) {
		String value = Optional.ofNullable(e.getValue())
			.map(Object::toString)
			.orElse("");

		List<ErrorDetailResponse.RequestFieldError> errors =
			ErrorDetailResponse.RequestFieldError.of(e.getName(), value, e.getErrorCode());

		return new ErrorDetailResponse(ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE), errors);
	}

	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	@Getter
	public static class RequestFieldError {

		private final String field;

		private final String value;

		private final String reason;

		private RequestFieldError(FieldError fieldError) {
			this.field = fieldError.getField();
			this.value = fieldError.getRejectedValue() == null ? "" : String.valueOf(fieldError.getRejectedValue());
			this.reason = fieldError.getDefaultMessage();
		}

		private static List<RequestFieldError> of(BindingResult bindingResult) {
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();

			return fieldErrors.stream()
				.map(RequestFieldError::new)
				.collect(Collectors.toList());
		}

		private static List<RequestFieldError> of(String field, String value, String reason) {
			List<RequestFieldError> requestFieldErrors = new ArrayList<>();
			requestFieldErrors.add(new RequestFieldError(field, value, reason));
			return requestFieldErrors;
		}
	}
}
