package com.hotel.demo.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.hotel.demo.infrastructure.persistence.entity.ReservaEntity;
import com.hotel.demo.model.Reserva;
import com.hotel.demo.repository.ReservaRepository;

@Repository
public class JpaReservaRepositoryAdapter implements ReservaRepository {
	private final ReservaJpaRepository reservaJpaRepository;
	private final ClienteJpaRepository clienteJpaRepository;

	public JpaReservaRepositoryAdapter(
			ReservaJpaRepository reservaJpaRepository,
			ClienteJpaRepository clienteJpaRepository) {
		this.reservaJpaRepository = reservaJpaRepository;
		this.clienteJpaRepository = clienteJpaRepository;
	}

	@Override
	public Optional<Reserva> findById(UUID id) {
		return reservaJpaRepository.findById(id).map(entity -> entity.toDomain());
	}

	@Override
	public Reserva save(Reserva reserva) {
		var cliente = clienteJpaRepository.getReferenceById(reserva.clienteId());
		return reservaJpaRepository.save(new ReservaEntity(reserva, cliente)).toDomain();
	}

	@Override
	public List<Reserva> findByClienteId(UUID clienteId) {
		return reservaJpaRepository.findByCliente_Id(clienteId).stream()
			.map(entity -> entity.toDomain())
			.toList();
	}
}