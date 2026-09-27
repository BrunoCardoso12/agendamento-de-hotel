package com.hotel.demo.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.demo.infrastructure.persistence.entity.ReservaEntity;

public interface ReservaJpaRepository extends JpaRepository<ReservaEntity, UUID> {
	List<ReservaEntity> findByCliente_Id(UUID clienteId);
}