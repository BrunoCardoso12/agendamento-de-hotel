package com.hotel.demo.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.hotel.demo.model.Hotel;

public interface HotelRepository {
	boolean existsByNomeAndCidade(String nome, String cidade);

	Optional<Hotel> findById(UUID id);

	List<Hotel> findAll();

	Hotel save(Hotel hotel);
}
