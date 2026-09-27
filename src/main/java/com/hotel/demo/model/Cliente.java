package com.hotel.demo.model;

import java.util.UUID;

public record Cliente(UUID id, String nomeCompleto, String cpf, String email) {
}
