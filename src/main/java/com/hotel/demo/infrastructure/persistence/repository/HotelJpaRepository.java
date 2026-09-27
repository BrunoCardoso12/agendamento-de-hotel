package com.hotel.demo.infrastructure.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.demo.infrastructure.persistence.entity.HotelEntity;

public interface HotelJpaRepository extends JpaRepository<HotelEntity, UUID> {
	boolean existsByNomeIgnoreCaseAndCidadeIgnoreCase(String nome, String cidade);
}