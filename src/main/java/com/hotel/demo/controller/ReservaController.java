package com.hotel.demo.controller;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.demo.model.Reserva;
import com.hotel.demo.service.ReservaService;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {
	private final ReservaService reservaService;

	public ReservaController(ReservaService reservaService) {
		this.reservaService = reservaService;
	}

	@PostMapping
	public ResponseEntity<Reserva> criar(@RequestBody CriarReservaRequest request) {
		Reserva reserva = reservaService.criar(request.clienteId(), request.destino(), request.dataIda());
		return ResponseEntity.created(URI.create("/api/reservas/" + reserva.id())).body(reserva);
	}

	@GetMapping("/cliente/{clienteId}")
	public List<Reserva> listarPorCliente(@PathVariable UUID clienteId) {
		return reservaService.listarPorCliente(clienteId);
	}

	public record CriarReservaRequest(UUID clienteId, String destino, LocalDate dataIda) {
	}
}
