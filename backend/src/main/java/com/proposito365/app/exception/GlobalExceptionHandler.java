package com.proposito365.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

	@ExceptionHandler(InvalidEmailException.class)
	public ResponseEntity<ApiError> handleInvalidEmail(InvalidEmailException ex) {
		ApiError apiError = new ApiError(ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
