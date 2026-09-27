package com.hotel.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record Pagamento(
	UUID id,
	UUID reservaId,
	BigDecimal valor,
	FormaPagamento formaPagamento,
	Status status,
	LocalDateTime criadoEm,
	LocalDateTime confirmadoEm) {

	public enum FormaPagamento {
		CARTAO,
		BOLETO
	}

	public enum Status {
		PENDENTE,
		PAGO
	}
}
