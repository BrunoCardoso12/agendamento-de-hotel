package com.hotel.demo.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.demo.model.Cliente;
import com.hotel.demo.service.ClienteService;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {
	private final ClienteService clienteService;

	public ClienteController(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	@PostMapping
	public ResponseEntity<ClienteResponse> cadastrar(@RequestBody CadastroClienteRequest request) {
		Cliente cliente = clienteService.cadastrar(request.nomeCompleto(), request.cpf(), request.email());
		return ResponseEntity.created(URI.create("/api/clientes/" + cliente.id())).body(toResponse(cliente));
	}

	@GetMapping("/{id}")
	public ClienteResponse buscar(@PathVariable UUID id) {
		return toResponse(clienteService.buscar(id));
	}

	private ClienteResponse toResponse(Cliente cliente) {
		String cpf = cliente.cpf();
		return new ClienteResponse(cliente.id(), cliente.nomeCompleto(), "***.***.***-" + cpf.substring(9), cliente.email());
	}

	public record CadastroClienteRequest(String nomeCompleto, String cpf, String email) {
	}

	public record ClienteResponse(UUID id, String nomeCompleto, String cpf, String email) {
	}
}
