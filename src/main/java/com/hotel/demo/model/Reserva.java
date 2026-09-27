package com.hotel.demo.model;

import java.time.LocalDate;
import java.util.UUID;

public record Reserva(UUID id, UUID clienteId, String destino, LocalDate dataIda, String status) {
}
