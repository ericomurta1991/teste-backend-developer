package com.desafio_dev.desafio_dev.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desafio_dev.desafio_dev.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByCpf(String cpf);
	Optional<User> findByCpf(String cpf);
}
