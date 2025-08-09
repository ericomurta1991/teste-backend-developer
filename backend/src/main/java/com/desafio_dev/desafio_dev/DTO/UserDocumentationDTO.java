package com.desafio_dev.desafio_dev.DTO;

import com.desafio_dev.desafio_dev.enums.DocumentType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDocumentationDTO {
	
	private Long id;
	@NotNull(message = "Tipo de Documento é obrigatório")
    private DocumentType type;
	
	@NotBlank(message = "Numero do documento  é obrigatório")
    private String number;
	
	@JsonIgnore
	@NotNull(message = "Documento é obrigatório")
	private byte[] document;
    private Long userId;
	    
  
    

    
    
    
    
    
    
    
    
}
