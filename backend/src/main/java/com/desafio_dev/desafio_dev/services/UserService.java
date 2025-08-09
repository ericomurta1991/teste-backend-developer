package com.desafio_dev.desafio_dev.services;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desafio_dev.desafio_dev.DTO.UserDTO;
import com.desafio_dev.desafio_dev.entities.User;
import com.desafio_dev.desafio_dev.repositories.UserRepository;
import com.desafio_dev.desafio_dev.services.exceptions.DatabaseException;
import com.desafio_dev.desafio_dev.services.exceptions.DesafioException;
import com.desafio_dev.desafio_dev.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService{
	
	
	@Autowired
	UserRepository repository;
	
	@Transactional
	public Page<UserDTO> findAllPaged(PageRequest pageRequest){
		Page<User> list = repository.findAll(pageRequest);
		return list.map(x -> new UserDTO(x));
	}
	
	@Transactional
	public UserDTO findById(Long id) {
		Optional<User> obj = repository.findById(id);
		User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity Not Found"));
		return new UserDTO(entity);
	}
	
	@Transactional
	public UserDTO insert(UserDTO dto) {
		
		if(repository.existsByCpf(dto.getCpf())) {
			throw new DesafioException("CPF já cadastrado");
		}
		
		User entity = new User();
		entity.setName(dto.getName());
		entity.setCpf(dto.getCpf());
		entity = repository.save(entity);
		
		return new UserDTO(entity);
	}
	
	@Transactional
	public UserDTO update(Long id, UserDTO dto) {
		try {
			User entity = repository.getReferenceById(id);
			
			Optional<User> userWithCpf = repository.findByCpf(dto.getCpf());
			if(userWithCpf.isPresent() && !userWithCpf.get().getId().equals(id)) {
				throw new DesafioException("CPF já cadastrado para outro usuário");
			}
			
			entity.setName(dto.getName());
			entity.setCpf(dto.getCpf());
			
			entity = repository.save(entity);
			return new UserDTO(entity);			
		} catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("ID not Found " + id);
		}
	}
	
	public void delete(Long id) {
		if(!repository.existsById(id)) {
			throw new ResourceNotFoundException("ID not Found: " + id);
		}
		
		
		try {
			repository.deleteById(id);
		}catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("ID Not Found: " + id);
		}catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity Violation");
		}
	}
	
	
}
