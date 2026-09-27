package com.hotel.demo.model;

import java.util.UUID;

public record Hotel(UUID id, String nome, String cidade, int quartosDisponiveis) {
}
