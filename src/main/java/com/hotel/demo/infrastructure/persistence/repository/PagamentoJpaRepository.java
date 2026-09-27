package com.hotel.demo.infrastructure.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.demo.infrastructure.persistence.entity.PagamentoEntity;

public interface PagamentoJpaRepository extends JpaRepository<PagamentoEntity, UUID> {
	boolean existsByReserva_Id(UUID reservaId);
}