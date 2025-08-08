package com.desafio_dev.desafio_dev.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.desafio_dev.desafio_dev.entities.UserDocumentation;

public interface UserDocumentationRepository extends JpaRepository<UserDocumentation, Long> {

}
