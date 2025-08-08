package com.desafio_dev.desafio_dev.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.desafio_dev.desafio_dev.DTO.UserDocumentationDTO;
import com.desafio_dev.desafio_dev.enums.DocumentType;
import com.desafio_dev.desafio_dev.services.UserDocumentationService;

@RestController
@RequestMapping("/user-documentation")
public class UserDocumentationResource {
	
	@Autowired
	private UserDocumentationService service;
	
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDocumentationDTO> create(
            @RequestParam("type") DocumentType type,
            @RequestParam("number") String number,
            @RequestParam("userId") Long userId,
            @RequestParam("file") MultipartFile file) {

        UserDocumentationDTO dto = service.create(type, number, userId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}
