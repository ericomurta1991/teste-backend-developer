package com.desafio_dev.desafio_dev.DTO;

import java.io.Serializable;

import com.desafio_dev.desafio_dev.entities.User;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO implements Serializable{
	private static final long serialVersionUID =1l;
	
	private long id;
	@NotBlank
	private String name;
	@NotBlank
	private String cpf;
	
	public UserDTO (User entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.cpf = entity.getCpf();
	}
}
