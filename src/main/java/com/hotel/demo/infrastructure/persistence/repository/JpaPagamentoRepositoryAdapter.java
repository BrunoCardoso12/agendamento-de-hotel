package com.hotel.demo.infrastructure.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.hotel.demo.infrastructure.persistence.entity.PagamentoEntity;
import com.hotel.demo.model.Pagamento;
import com.hotel.demo.repository.PagamentoRepository;

@Repository
public class JpaPagamentoRepositoryAdapter implements PagamentoRepository {
	private final PagamentoJpaRepository pagamentoJpaRepository;
	private final ReservaJpaRepository reservaJpaRepository;

	public JpaPagamentoRepositoryAdapter(
			PagamentoJpaRepository pagamentoJpaRepository,
			ReservaJpaRepository reservaJpaRepository) {
		this.pagamentoJpaRepository = pagamentoJpaRepository;
		this.reservaJpaRepository = reservaJpaRepository;
	}

	@Override
	public boolean existsByReservaId(UUID reservaId) {
		return pagamentoJpaRepository.existsByReserva_Id(reservaId);
	}

	@Override
	public Optional<Pagamento> findById(UUID id) {
		return pagamentoJpaRepository.findById(id).map(entity -> entity.toDomain());
	}

	@Override
	public Pagamento save(Pagamento pagamento) {
		var reserva = reservaJpaRepository.getReferenceById(pagamento.reservaId());
		return pagamentoJpaRepository.save(new PagamentoEntity(pagamento, reserva)).toDomain();
	}
}