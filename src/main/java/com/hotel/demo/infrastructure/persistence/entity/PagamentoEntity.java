package com.hotel.demo.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.hotel.demo.model.Pagamento;
import com.hotel.demo.model.Pagamento.FormaPagamento;
import com.hotel.demo.model.Pagamento.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "pagamentos", uniqueConstraints = @UniqueConstraint(
	name = "uk_pagamento_reserva", columnNames = "reserva_id"))
public class PagamentoEntity {
	@Id
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "reserva_id", nullable = false)
	private ReservaEntity reserva;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal valor;

	@Enumerated(EnumType.STRING)
	@Column(name = "forma_pagamento", nullable = false)
	private FormaPagamento formaPagamento;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;

	@Column(name = "criado_em", nullable = false)
	private LocalDateTime criadoEm;

	@Column(name = "confirmado_em")
	private LocalDateTime confirmadoEm;

	protected PagamentoEntity() {
	}

	public PagamentoEntity(Pagamento pagamento, ReservaEntity reserva) {
		this.id = pagamento.id();
		this.reserva = reserva;
		this.valor = pagamento.valor();
		this.formaPagamento = pagamento.formaPagamento();
		this.status = pagamento.status();
		this.criadoEm = pagamento.criadoEm();
		this.confirmadoEm = pagamento.confirmadoEm();
	}

	public Pagamento toDomain() {
		return new Pagamento(id, reserva.getId(), valor, formaPagamento, status, criadoEm, confirmadoEm);
	}
}