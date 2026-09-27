package com.hotel.demo.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.hotel.demo.model.Reserva;

public interface ReservaRepository {
	Optional<Reserva> findById(UUID id);

	Reserva save(Reserva reserva);

	List<Reserva> findByClienteId(UUID clienteId);
}
