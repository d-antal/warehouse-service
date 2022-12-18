package com.vodafone.exercise.warehouse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DeviceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DeviceNotFoundException(String message) {
		super(message);
	}
}
