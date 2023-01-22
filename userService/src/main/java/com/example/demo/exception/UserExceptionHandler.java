package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler {
	
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<Object> badException(BadRequestException exception) {
		return  new ResponseEntity<>("Exception "+exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> exception(Exception exception) {
		return  new ResponseEntity<>("Exception "+exception.getMessage(), HttpStatus.BAD_REQUEST);
	}
}

