package com.desafio_dev.desafio_dev.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	
	  public Page<UserDocumentationDTO> findAll(Pageable pageable) {
	        return documentationRepository.findAll(pageable)
	                .map(doc -> new UserDocumentationDTO(
	                		doc.getId(), 
	                		doc.getType(), 
	                		doc.getNumber(), 
	                		doc.getDocument(), 
	                		doc.getUser().getId()));
	    }
	
	  
	  public UserDocumentationDTO findById(Long id) {
		    UserDocumentation entity = documentationRepository.findById(id)
		        .orElseThrow(() -> new ResourceNotFoundException("Documento não encontrado"));
		    return new UserDocumentationDTO(entity.getId(), entity.getType(), entity.getNumber(), entity.getDocument(), entity.getUser().getId());
		}
	
	
	

	@Transactional
    public UserDocumentationDTO insert(DocumentType type, String number, Long userId, MultipartFile file) {
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
	
	@Transactional
	public UserDocumentationDTO update(Long id, DocumentType type, String number, MultipartFile file) {
		 // Buscar o documento existente
	    UserDocumentation entity = documentationRepository.findById(id)
	        .orElseThrow(() -> new ResourceNotFoundException("Documento não encontrado"));

	    // Validar o número do documento (sempre obrigatório)
	    if (number == null || number.trim().isEmpty()) {
	        throw new DesafioException("Número do documento é obrigatório");
	    }

	    // Se veio arquivo e não está vazio, validar tamanho e atualizar
	    if (file != null && !file.isEmpty()) {
	        if (file.getSize() > MAX_FILE_SIZE) {
	            throw new DesafioException("Arquivo não pode exceder 2MB");
	        }
	        try {
	            entity.setDocument(file.getBytes());
	        } catch (IOException e) {
	            throw new DesafioException("Erro ao processar o arquivo");
	        }
	    }
	    // Se não enviou arquivo, mantém o arquivo atual sem alteração

	    // Atualiza tipo e número sempre
	    entity.setType(type);
	    entity.setNumber(number);

	    // Salva e retorna DTO atualizado
	    entity = documentationRepository.save(entity);

	    return new UserDocumentationDTO(entity.getId(), entity.getType(), entity.getNumber(), entity.getDocument(), entity.getUser().getId());
	}
	
	@Transactional
	public void delete(Long id) {
	    try {
	        documentationRepository.deleteById(id);
	    } catch (EmptyResultDataAccessException e) {
	        throw new ResourceNotFoundException("Documento não encontrado");
	    } catch (DataIntegrityViolationException e) {
	        throw new DatabaseException("Erro de integridade ao deletar documento");
	    }
	}
	
	
	
}