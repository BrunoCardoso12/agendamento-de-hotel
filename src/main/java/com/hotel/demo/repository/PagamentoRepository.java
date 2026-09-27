package com.hotel.demo.repository;

import java.util.Optional;
import java.util.UUID;

import com.hotel.demo.model.Pagamento;

public interface PagamentoRepository {
	boolean existsByReservaId(UUID reservaId);

	Optional<Pagamento> findById(UUID id);

	Pagamento save(Pagamento pagamento);
}
