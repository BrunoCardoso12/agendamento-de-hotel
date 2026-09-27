package com.hotel.demo.infrastructure.persistence.entity;

import java.util.UUID;

import com.hotel.demo.model.Cliente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "clientes", uniqueConstraints = {
	@UniqueConstraint(name = "uk_cliente_cpf", columnNames = "cpf"),
	@UniqueConstraint(name = "uk_cliente_email", columnNames = "email")
})
public class ClienteEntity {
	@Id
	private UUID id;

	@Column(name = "nome_completo", nullable = false)
	private String nomeCompleto;

	@Column(nullable = false, length = 11)
	private String cpf;

	@Column(nullable = false)
	private String email;

	protected ClienteEntity() {
	}

	public ClienteEntity(Cliente cliente) {
		this.id = cliente.id();
		this.nomeCompleto = cliente.nomeCompleto();
		this.cpf = cliente.cpf();
		this.email = cliente.email();
	}

	public Cliente toDomain() {
		return new Cliente(id, nomeCompleto, cpf, email);
	}

	public UUID getId() {
		return id;
	}
}