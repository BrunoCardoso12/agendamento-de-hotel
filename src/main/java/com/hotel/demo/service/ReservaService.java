package com.hotel.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hotel.demo.exception.InvalidRequestException;
import com.hotel.demo.exception.ResourceNotFoundException;
import com.hotel.demo.model.Reserva;
import com.hotel.demo.repository.ReservaRepository;

@Service
public class ReservaService {
	private final ClienteService clienteService;
	private final ReservaRepository reservaRepository;

	public ReservaService(ClienteService clienteService, ReservaRepository reservaRepository) {
		this.clienteService = clienteService;
		this.reservaRepository = reservaRepository;
	}

	public Reserva criar(UUID clienteId, String destino, LocalDate dataIda) {
		clienteService.buscar(clienteId);
		if (destino == null || destino.isBlank() || dataIda == null || !dataIda.isAfter(LocalDate.now())) {
			throw new InvalidRequestException("Destino e data futura de ida sao obrigatorios.");
		}

		Reserva reserva = new Reserva(UUID.randomUUID(), clienteId, destino.trim(), dataIda, "PENDENTE_PAGAMENTO");
		return reservaRepository.save(reserva);
	}

	public List<Reserva> listarPorCliente(UUID clienteId) {
		clienteService.buscar(clienteId);
		return reservaRepository.findByClienteId(clienteId);
	}

	public Reserva buscar(UUID id) {
		return reservaRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Reserva nao encontrada."));
	}

	public Reserva confirmarPagamento(UUID id) {
		Reserva reserva = buscar(id);
		if (!reserva.status().equals("PENDENTE_PAGAMENTO")) {
			throw new InvalidRequestException("A reserva nao esta aguardando pagamento.");
		}

		Reserva confirmada = new Reserva(
			reserva.id(), reserva.clienteId(), reserva.destino(), reserva.dataIda(), "CONFIRMADA");
		return reservaRepository.save(confirmada);
	}
}
