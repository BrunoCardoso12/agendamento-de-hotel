package com.hotel.demo.infrastructure.persistence.entity;

import java.util.UUID;

import com.hotel.demo.model.Hotel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "hoteis", uniqueConstraints = @UniqueConstraint(
	name = "uk_hotel_nome_cidade", columnNames = { "nome", "cidade" }))
public class HotelEntity {
	@Id
	private UUID id;

	@Column(nullable = false)
	private String nome;

	@Column(nullable = false)
	private String cidade;

	@Column(name = "quartos_disponiveis", nullable = false)
	private int quartosDisponiveis;

	protected HotelEntity() {
	}

	public HotelEntity(Hotel hotel) {
		this.id = hotel.id();
		this.nome = hotel.nome();
		this.cidade = hotel.cidade();
		this.quartosDisponiveis = hotel.quartosDisponiveis();
	}

	public Hotel toDomain() {
		return new Hotel(id, nome, cidade, quartosDisponiveis);
	}
}