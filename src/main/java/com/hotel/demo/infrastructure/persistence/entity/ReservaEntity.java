package com.hotel.demo.infrastructure.persistence.entity;

import java.time.LocalDate;
import java.util.UUID;

import com.hotel.demo.model.Reserva;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservas")
public class ReservaEntity {
	@Id
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "cliente_id", nullable = false)
	private ClienteEntity cliente;

	@Column(nullable = false)
	private String destino;

	@Column(name = "data_ida", nullable = false)
	private LocalDate dataIda;

	@Column(nullable = false)
	private String status;

	protected ReservaEntity() {
	}

	public ReservaEntity(Reserva reserva, ClienteEntity cliente) {
		this.id = reserva.id();
		this.cliente = cliente;
		this.destino = reserva.destino();
		this.dataIda = reserva.dataIda();
		this.status = reserva.status();
	}

	public Reserva toDomain() {
		return new Reserva(id, cliente.getId(), destino, dataIda, status);
	}

	public UUID getId() {
		return id;
	}
}