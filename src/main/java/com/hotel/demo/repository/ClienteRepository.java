package com.hotel.demo.repository;

import java.util.Optional;
import java.util.UUID;

import com.hotel.demo.model.Cliente;

public interface ClienteRepository {
	Optional<Cliente> findById(UUID id);

	boolean existsByCpfOrEmail(String cpf, String email);

	Cliente save(Cliente cliente);
}
