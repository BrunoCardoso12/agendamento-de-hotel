package com.hotel.demo.infrastructure.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.hotel.demo.infrastructure.persistence.entity.ClienteEntity;
import com.hotel.demo.model.Cliente;
import com.hotel.demo.repository.ClienteRepository;

@Repository
public class JpaClienteRepositoryAdapter implements ClienteRepository {
	private final ClienteJpaRepository clienteJpaRepository;

	public JpaClienteRepositoryAdapter(ClienteJpaRepository clienteJpaRepository) {
		this.clienteJpaRepository = clienteJpaRepository;
	}

	@Override
	public Optional<Cliente> findById(UUID id) {
		return clienteJpaRepository.findById(id).map(entity -> entity.toDomain());
	}

	@Override
	public boolean existsByCpfOrEmail(String cpf, String email) {
		return clienteJpaRepository.existsByCpfOrEmail(cpf, email);
	}

	@Override
	public Cliente save(Cliente cliente) {
		return clienteJpaRepository.save(new ClienteEntity(cliente)).toDomain();
	}
}