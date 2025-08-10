package com.desafio_dev.desafio_dev.resources.exceptions;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

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
		err.setError("Not found");
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
	    err.setError("Bad Request");
	    err.setMessage(e.getMessage());
	    err.setPath(request.getRequestURI());

	    return ResponseEntity.status(status).body(err);
	}
	
	// Handler para erros de validação do Bean Validation
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> handleValidationExceptions(MethodArgumentNotValidException e, HttpServletRequest request) {
	    HttpStatus status = HttpStatus.BAD_REQUEST;
	    ValidationError err = new ValidationError();
	    err.setTimestamp(Instant.now());
	    err.setStatus(status.value());
	    err.setError("Validation exception");
	    err.setMessage("Erro de validação nos campos");
	    err.setPath(request.getRequestURI());

	    for (FieldError f : e.getBindingResult().getFieldErrors()) {
	        err.addError(f.getField(), f.getDefaultMessage());
	    }

	    return ResponseEntity.status(status).body(err);
	}
	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<StandardError> handleAllExceptions(Exception e, HttpServletRequest request){
	    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

	    StandardError err = new StandardError();
	    err.setTimestamp(Instant.now());
	    err.setStatus(status.value());
	    err.setError("Internal Server Error");
	    err.setMessage("Erro inesperado, entre em contato com o suporte.");
	    err.setPath(request.getRequestURI());

	    return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<StandardError> handleMaxSizeException(MaxUploadSizeExceededException e, HttpServletRequest request) {
	    HttpStatus status = HttpStatus.BAD_REQUEST;

	    StandardError err = new StandardError();
	    err.setTimestamp(Instant.now());
	    err.setStatus(status.value());
	    err.setError("Bad Request");
	    err.setMessage("Arquivo não pode exceder 2MB");
	    err.setPath(request.getRequestURI());

	    return ResponseEntity.status(status).body(err);
	}
	
	 @ExceptionHandler(NoHandlerFoundException.class)
	    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex) {
	        Map<String, Object> body = new HashMap<>();
	        body.put("timestamp", Instant.now());
	        body.put("status", HttpStatus.NOT_FOUND.value());
	        body.put("error", "Recurso não encontrado");
	        body.put("message", "A rota " + ex.getRequestURL() + " não existe");
	        body.put("path", ex.getRequestURL());

	        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	    }

	
}
