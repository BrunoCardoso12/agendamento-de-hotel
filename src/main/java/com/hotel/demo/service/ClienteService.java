package com.hotel.demo.service;

import java.util.Locale;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hotel.demo.exception.DuplicateResourceException;
import com.hotel.demo.exception.InvalidRequestException;
import com.hotel.demo.exception.ResourceNotFoundException;
import com.hotel.demo.model.Cliente;
import com.hotel.demo.repository.ClienteRepository;
import com.ifsp.edu.service.CpfValidationService;

@Service
public class ClienteService {
	private final CpfValidationService cpfValidationService;
	private final ClienteRepository clienteRepository;

	public ClienteService(CpfValidationService cpfValidationService, ClienteRepository clienteRepository) {
		this.cpfValidationService = cpfValidationService;
		this.clienteRepository = clienteRepository;
	}

	public synchronized Cliente cadastrar(String nomeCompleto, String cpf, String email) {
		if (nomeCompleto == null || nomeCompleto.isBlank() || email == null || email.isBlank()) {
			throw new InvalidRequestException("Nome e e-mail sao obrigatorios.");
		}
		if (!cpfValidationService.isValid(cpf)) {
			throw new InvalidRequestException("Informe um CPF valido.");
		}

		String cpfNormalizado = cpf.replaceAll("\\D", "");
		String emailNormalizado = email.trim().toLowerCase(Locale.ROOT);
		if (clienteRepository.existsByCpfOrEmail(cpfNormalizado, emailNormalizado)) {
			throw new DuplicateResourceException("CPF ou e-mail ja cadastrado.");
		}

		Cliente cliente = new Cliente(UUID.randomUUID(), nomeCompleto.trim(), cpfNormalizado, emailNormalizado);
		return clienteRepository.save(cliente);
	}

	public Cliente buscar(UUID id) {
		return clienteRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Cliente nao encontrado."));
	}
}
