package com.hotel.demo.service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hotel.demo.exception.DuplicateResourceException;
import com.hotel.demo.exception.InvalidRequestException;
import com.hotel.demo.exception.ResourceNotFoundException;
import com.hotel.demo.model.Hotel;
import com.hotel.demo.repository.HotelRepository;

@Service
public class HotelService {
	private final HotelRepository hotelRepository;

	public HotelService(HotelRepository hotelRepository) {
		this.hotelRepository = hotelRepository;
	}

	public Hotel cadastrar(String nome, String cidade, int quartosDisponiveis) {
		if (nome == null || nome.isBlank() || cidade == null || cidade.isBlank() || quartosDisponiveis < 1) {
			throw new InvalidRequestException("Nome, cidade e ao menos um quarto sao obrigatorios.");
		}

		String nomeNormalizado = nome.trim();
		String cidadeNormalizada = cidade.trim();
		if (hotelRepository.existsByNomeAndCidade(
			nomeNormalizado.toLowerCase(Locale.ROOT), cidadeNormalizada.toLowerCase(Locale.ROOT))) {
			throw new DuplicateResourceException("Hotel ja cadastrado nessa cidade.");
		}

		return hotelRepository.save(new Hotel(
			UUID.randomUUID(), nomeNormalizado, cidadeNormalizada, quartosDisponiveis));
	}

	public List<Hotel> listar() {
		return hotelRepository.findAll();
	}

	public Hotel buscar(UUID id) {
		return hotelRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Hotel nao encontrado."));
	}
}
