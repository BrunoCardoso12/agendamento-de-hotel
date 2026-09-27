package com.hotel.demo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotel.demo.exception.DuplicateResourceException;
import com.hotel.demo.exception.InvalidRequestException;
import com.hotel.demo.exception.ResourceNotFoundException;
import com.hotel.demo.model.Pagamento;
import com.hotel.demo.model.Pagamento.FormaPagamento;
import com.hotel.demo.model.Pagamento.Status;
import com.hotel.demo.repository.PagamentoRepository;

@Service
public class PagamentoService {
	private final PagamentoRepository pagamentoRepository;
	private final ReservaService reservaService;

	public PagamentoService(PagamentoRepository pagamentoRepository, ReservaService reservaService) {
		this.pagamentoRepository = pagamentoRepository;
		this.reservaService = reservaService;
	}

	@Transactional
	public Pagamento registrar(UUID reservaId, BigDecimal valor, FormaPagamento formaPagamento) {
		reservaService.buscar(reservaId);
		if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0 || formaPagamento == null) {
			throw new InvalidRequestException("Informe valor positivo e forma de pagamento.");
		}
		if (pagamentoRepository.existsByReservaId(reservaId)) {
			throw new DuplicateResourceException("Ja existe um pagamento para esta reserva.");
		}

		LocalDateTime agora = LocalDateTime.now();
		boolean cartao = formaPagamento == FormaPagamento.CARTAO;
		Pagamento pagamento = new Pagamento(
			UUID.randomUUID(), reservaId, valor, formaPagamento,
			cartao ? Status.PAGO : Status.PENDENTE, agora, cartao ? agora : null);
		Pagamento salvo = pagamentoRepository.save(pagamento);
		if (cartao) {
			reservaService.confirmarPagamento(reservaId);
		}
		return salvo;
	}

	@Transactional
	public Pagamento confirmarBoleto(UUID id) {
		Pagamento pagamento = buscar(id);
		if (pagamento.formaPagamento() != FormaPagamento.BOLETO || pagamento.status() != Status.PENDENTE) {
			throw new InvalidRequestException("Somente boletos pendentes podem ser confirmados.");
		}

		reservaService.confirmarPagamento(pagamento.reservaId());
		return pagamentoRepository.save(new Pagamento(
			pagamento.id(), pagamento.reservaId(), pagamento.valor(), pagamento.formaPagamento(),
			Status.PAGO, pagamento.criadoEm(), LocalDateTime.now()));
	}

	public Pagamento buscar(UUID id) {
		return pagamentoRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Pagamento nao encontrado."));
	}
}
