package com.desafio_dev.desafio_dev.services.exceptions;

public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1l;
	
	public ResourceNotFoundException(String msg) {
		super(msg);
	}
}
