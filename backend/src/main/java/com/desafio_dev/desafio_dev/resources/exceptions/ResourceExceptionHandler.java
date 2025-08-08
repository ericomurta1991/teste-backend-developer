package com.desafio_dev.desafio_dev.resources.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.desafio_dev.desafio_dev.services.exceptions.DatabaseException;
import com.desafio_dev.desafio_dev.services.exceptions.DesafioException;
import com.desafio_dev.desafio_dev.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {
		
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> handleResourceNotFound(ResourceNotFoundException e, HttpServletRequest request){
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(HttpStatus.NOT_FOUND.value());
		err.setError("Resource not found");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		
		return ResponseEntity.status(status).body(err);
		
	}
	
	@ExceptionHandler(DatabaseException.class) 
	public ResponseEntity<StandardError> handleDatabaseException(DatabaseException e, HttpServletRequest request){
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Database exception");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(DesafioException.class)
	public ResponseEntity<StandardError> handleDesafioException(DesafioException e, HttpServletRequest request){
	    HttpStatus status = HttpStatus.BAD_REQUEST;  // 400 para erros de validação ou regras de negócio

	    StandardError err = new StandardError();
	    err.setTimestamp(Instant.now());
	    err.setStatus(status.value());
	    err.setError("Application error");
	    err.setMessage(e.getMessage());
	    err.setPath(request.getRequestURI());

	    return ResponseEntity.status(status).body(err);
	}
	
	
}
