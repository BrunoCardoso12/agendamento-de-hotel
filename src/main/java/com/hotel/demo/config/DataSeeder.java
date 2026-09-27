package com.hotel.demo.config;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.hotel.demo.model.Cliente;
import com.hotel.demo.model.Pagamento.FormaPagamento;
import com.hotel.demo.model.Reserva;
import com.hotel.demo.service.ClienteService;
import com.hotel.demo.service.HotelService;
import com.hotel.demo.service.PagamentoService;
import com.hotel.demo.service.ReservaService;

// Seeds 3 sample records per entity on startup to speed up manual testing; skipped if data already exists.
@Component
public class DataSeeder implements CommandLineRunner {
	private final ClienteService clienteService;
	private final HotelService hotelService;
	private final ReservaService reservaService;
	private final PagamentoService pagamentoService;

	public DataSeeder(
			ClienteService clienteService,
			HotelService hotelService,
			ReservaService reservaService,
			PagamentoService pagamentoService) {
		this.clienteService = clienteService;
		this.hotelService = hotelService;
		this.reservaService = reservaService;
		this.pagamentoService = pagamentoService;
	}

	@Override
	public void run(String... args) {
		if (!hotelService.listar().isEmpty()) {
			return;
		}

		hotelService.cadastrar("Hotel Vista Mar", "Florianopolis", 12);
		hotelService.cadastrar("Hotel Serra Azul", "Gramado", 8);
		hotelService.cadastrar("Hotel Central Plaza", "Sao Paulo", 20);

		Cliente bruno = clienteService.cadastrar("Bruno Oliveira", "111.444.777-35", "bruno.oliveira@example.com");
		Cliente carla = clienteService.cadastrar("Carla Souza", "987.654.321-00", "carla.souza@example.com");
		Cliente diego = clienteService.cadastrar("Diego Santos", "123.456.789-09", "diego.santos@example.com");

		Reserva reserva1 = reservaService.criar(bruno.id(), "Florianopolis", LocalDate.now().plusDays(30));
		Reserva reserva2 = reservaService.criar(carla.id(), "Gramado", LocalDate.now().plusDays(45));
		Reserva reserva3 = reservaService.criar(diego.id(), "Sao Paulo", LocalDate.now().plusDays(60));

		pagamentoService.registrar(reserva1.id(), new BigDecimal("1500.00"), FormaPagamento.CARTAO);
		pagamentoService.registrar(reserva2.id(), new BigDecimal("2200.50"), FormaPagamento.BOLETO);
		pagamentoService.registrar(reserva3.id(), new BigDecimal("980.00"), FormaPagamento.CARTAO);
	}
}
