package com.vodafone.exercise.warehouse.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class DeviceExceptionHander extends ResponseEntityExceptionHandler {

	@ExceptionHandler(DeviceNotFoundException.class)
	public ResponseEntity<Object> resourceNotFoundException(Exception ex, WebRequest request) {
		ErrorResponse errorResponse = new ErrorResponse(new Date(), ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

}
