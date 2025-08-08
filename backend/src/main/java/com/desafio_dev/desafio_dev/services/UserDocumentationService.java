package com.desafio_dev.desafio_dev.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.desafio_dev.desafio_dev.DTO.UserDocumentationDTO;
import com.desafio_dev.desafio_dev.entities.User;
import com.desafio_dev.desafio_dev.entities.UserDocumentation;
import com.desafio_dev.desafio_dev.enums.DocumentType;
import com.desafio_dev.desafio_dev.repositories.UserDocumentationRepository;
import com.desafio_dev.desafio_dev.repositories.UserRepository;
import com.desafio_dev.desafio_dev.services.exceptions.DatabaseException;
import com.desafio_dev.desafio_dev.services.exceptions.DesafioException;
import com.desafio_dev.desafio_dev.services.exceptions.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class UserDocumentationService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	UserDocumentationRepository documentationRepository;
	
	private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB
	
	
	public UserDocumentationService(UserRepository userRepository, UserDocumentationRepository documentationRepository) {
        this.userRepository = userRepository;
        this.documentationRepository = documentationRepository;
    }
	
	
	@Transactional
    public UserDocumentationDTO create(DocumentType type, String number, Long userId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new DesafioException("Arquivo é obrigatório");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new DesafioException("Arquivo não pode exceder 2MB");
        }

	
	User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

    try {
        UserDocumentation entity = new UserDocumentation();
        entity.setType(type);
        entity.setNumber(number);
        entity.setDocument(file.getBytes());
        entity.setUser(user);

        entity = documentationRepository.save(entity);
    
        return new UserDocumentationDTO(entity.getId(), entity.getType(), entity.getNumber(), entity.getDocument(), user.getId());
    } catch(IOException e) {
    	throw new DesafioException("Erro ao processar o arquivo");
    }catch (DataIntegrityViolationException e) {
    	throw new DatabaseException("Erro de integridade ao salvar documento");
    }
	}
	
}