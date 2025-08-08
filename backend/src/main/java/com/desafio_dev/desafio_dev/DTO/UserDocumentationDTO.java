package com.desafio_dev.desafio_dev.DTO;

import com.desafio_dev.desafio_dev.enums.DocumentType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
	
	@NotNull(message = "Documento é obrigatório")
	@Size(max = 2 * 1024 * 1024, message = "Documento não pode ultrapassar 2MB")
	private byte[] document;
    private Long userId;
	    
  
    

    
    
    
    
    
    
    
    
}
