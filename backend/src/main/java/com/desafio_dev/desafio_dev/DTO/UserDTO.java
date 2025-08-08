package com.desafio_dev.desafio_dev.DTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.desafio_dev.desafio_dev.entities.User;

import br.com.caelum.stella.bean.validation.CPF;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO implements Serializable{
	private static final long serialVersionUID =1L;
	
	private long id;
	@NotBlank
	@Size(max = 50, message = "Nome deve ter no maximo 50 caracteres")
	private String name;
	@NotBlank(message = "CPF é obrigatório")
	@CPF(message = "CPF inválido")
	private String cpf;
	
	private List<UserDocumentationDTO> documentations = new ArrayList<>();
	
	
	public UserDTO (User entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.cpf = entity.getCpf();
		
		this.documentations = entity.getDocumentations().stream()
	                .map(doc -> new UserDocumentationDTO(
	                        doc.getId(),
	                        doc.getType(),
	                        doc.getNumber(),
	                        doc.getDocument(),
	                        entity.getId()))
	                .collect(Collectors.toList());
	}
}
