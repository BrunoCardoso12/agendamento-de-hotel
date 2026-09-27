package com.hotel.demo.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.hotel.demo.infrastructure.persistence.entity.HotelEntity;
import com.hotel.demo.model.Hotel;
import com.hotel.demo.repository.HotelRepository;

@Repository
public class JpaHotelRepositoryAdapter implements HotelRepository {
	private final HotelJpaRepository hotelJpaRepository;

	public JpaHotelRepositoryAdapter(HotelJpaRepository hotelJpaRepository) {
		this.hotelJpaRepository = hotelJpaRepository;
	}

	@Override
	public boolean existsByNomeAndCidade(String nome, String cidade) {
		return hotelJpaRepository.existsByNomeIgnoreCaseAndCidadeIgnoreCase(nome, cidade);
	}

	@Override
	public Optional<Hotel> findById(UUID id) {
		return hotelJpaRepository.findById(id).map(entity -> entity.toDomain());
	}

	@Override
	public List<Hotel> findAll() {
		return hotelJpaRepository.findAll(Sort.by("nome")).stream()
			.map(entity -> entity.toDomain())
			.toList();
	}

	@Override
	public Hotel save(Hotel hotel) {
		return hotelJpaRepository.save(new HotelEntity(hotel)).toDomain();
	}
}