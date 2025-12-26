package com.proposito365.app.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	/* Custom Global Exceptions */

	@ExceptionHandler(BadRequestCustomException.class)
	public ResponseEntity<ApiError> handleBadRequestException(BadRequestCustomException ex) {
		ApiError apiError = new ApiError(ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	/* User Exceptions */

	@ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(UserNotFoundException ex) {
		ApiError apiError = new ApiError("USER_NOT_FOUND", "User not found");
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

	@ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<ApiError> handleInvalidUser(InvalidUserException ex) {
		ApiError apiError = new ApiError("INVALID_USER", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

	/* Group Exceptions */

	@ExceptionHandler(GroupNotFoundException.class)
    public ResponseEntity<ApiError> handleGroupNotFound(GroupNotFoundException ex) {
		ApiError apiError = new ApiError("GROUP_NOT_FOUND", "Group doesn't exists");
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

	@ExceptionHandler(InvalidGroupAdminException.class)
    public ResponseEntity<ApiError> handleInvalidGroupAdmin(InvalidGroupAdminException ex) {
		ApiError apiError = new ApiError("INVALID_ADMIN", ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

	@ExceptionHandler(GroupFullCapacityException.class)
	public ResponseEntity<ApiError> handleGroupCapacity(GroupFullCapacityException ex) {
		ApiError apiError = new ApiError("GROUP_IS_FULL", "The group capacity has reached its limit");
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

	/* Resolution Exceptions */

	@ExceptionHandler(ResolutionNotFoundException.class)
    public ResponseEntity<ApiError> handleResolutionNotFound(ResolutionNotFoundException ex) {
		ApiError apiError = new ApiError("RESOLUTION_NOT_FOUND", "Resolution not found");
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

	/* User-Groups Relation Exceptions */

	@ExceptionHandler(UserGroupRelationException.class)
    public ResponseEntity<ApiError> handleUserGroupRelation(UserGroupRelationException ex) {
		ApiError apiError = new ApiError("INVALID_RELATION", "User don't belong to the specified group");
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

	/* EmailVerification Exceptions */

	@ExceptionHandler(InvalidEmailException.class)
	public ResponseEntity<ApiError> handleInvalidEmail(InvalidEmailException ex) {
		ApiError apiError = new ApiError(ex.getCode(), ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
