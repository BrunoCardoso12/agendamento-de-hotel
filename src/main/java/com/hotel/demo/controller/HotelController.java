package com.hotel.demo.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.demo.model.Hotel;
import com.hotel.demo.service.HotelService;

@RestController
@RequestMapping("/api/hoteis")
public class HotelController {
	private final HotelService hotelService;

	public HotelController(HotelService hotelService) {
		this.hotelService = hotelService;
	}

	@PostMapping
	public ResponseEntity<Hotel> cadastrar(@RequestBody CadastroHotelRequest request) {
		Hotel hotel = hotelService.cadastrar(request.nome(), request.cidade(), request.quartosDisponiveis());
		return ResponseEntity.created(URI.create("/api/hoteis/" + hotel.id())).body(hotel);
	}

	@GetMapping
	public List<Hotel> listar() {
		return hotelService.listar();
	}

	@GetMapping("/{id}")
	public Hotel buscar(@PathVariable UUID id) {
		return hotelService.buscar(id);
	}

	public record CadastroHotelRequest(String nome, String cidade, int quartosDisponiveis) {
	}
}
