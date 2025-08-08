package com.desafio_dev.desafio_dev.enums;

public enum DocumentType {
	CPF("CPF"),
	CNPJ("CNPJ"),
	OUTROS("OUTROS");
	
	
	private final String description;
	

    DocumentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
