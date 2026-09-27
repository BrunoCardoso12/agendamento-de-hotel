package com.hotel.demo.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.demo.model.Pagamento;
import com.hotel.demo.model.Pagamento.FormaPagamento;
import com.hotel.demo.service.PagamentoService;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {
	private final PagamentoService pagamentoService;

	public PagamentoController(PagamentoService pagamentoService) {
		this.pagamentoService = pagamentoService;
	}

	@PostMapping
	public ResponseEntity<Pagamento> registrar(@RequestBody RegistrarPagamentoRequest request) {
		Pagamento pagamento = pagamentoService.registrar(
			request.reservaId(), request.valor(), request.formaPagamento());
		return ResponseEntity.created(URI.create("/api/pagamentos/" + pagamento.id())).body(pagamento);
	}

	@PostMapping("/{id}/confirmar-boleto")
	public Pagamento confirmarBoleto(@PathVariable UUID id) {
		return pagamentoService.confirmarBoleto(id);
	}

	@GetMapping("/{id}")
	public Pagamento buscar(@PathVariable UUID id) {
		return pagamentoService.buscar(id);
	}

	public record RegistrarPagamentoRequest(UUID reservaId, BigDecimal valor, FormaPagamento formaPagamento) {
	}
}
