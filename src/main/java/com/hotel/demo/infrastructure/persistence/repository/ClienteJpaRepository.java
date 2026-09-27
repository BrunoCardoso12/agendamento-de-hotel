package com.hotel.demo.infrastructure.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.demo.infrastructure.persistence.entity.ClienteEntity;

public interface ClienteJpaRepository extends JpaRepository<ClienteEntity, UUID> {
	boolean existsByCpfOrEmail(String cpf, String email);
}