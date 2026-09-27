package com.hotel.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ifsp.edu.service.CpfValidationService;

@RestController
@RequestMapping("/api/cpfs")
public class CpfController {
	private final CpfValidationService cpfValidationService;

	public CpfController(CpfValidationService cpfValidationService) {
		this.cpfValidationService = cpfValidationService;
	}

	@PostMapping("/validar")
	public CpfValidationResponse validar(@RequestBody CpfRequest request) {
		return new CpfValidationResponse(cpfValidationService.isValid(request.cpf()));
	}

	public record CpfRequest(String cpf) {
	}

	public record CpfValidationResponse(boolean valido) {
	}
}
