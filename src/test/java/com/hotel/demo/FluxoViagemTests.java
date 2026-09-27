package com.hotel.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hotel.demo.exception.DuplicateResourceException;
import com.hotel.demo.exception.InvalidRequestException;
import com.hotel.demo.exception.ResourceNotFoundException;
import com.hotel.demo.infrastructure.persistence.repository.ClienteJpaRepository;
import com.hotel.demo.infrastructure.persistence.repository.HotelJpaRepository;
import com.hotel.demo.infrastructure.persistence.repository.PagamentoJpaRepository;
import com.hotel.demo.infrastructure.persistence.repository.ReservaJpaRepository;
import com.hotel.demo.model.Cliente;
import com.hotel.demo.model.Hotel;
import com.hotel.demo.model.Pagamento;
import com.hotel.demo.model.Reserva;
import com.hotel.demo.service.ClienteService;
import com.hotel.demo.service.HotelService;
import com.hotel.demo.service.PagamentoService;
import com.hotel.demo.service.ReservaService;
import com.ifsp.edu.service.CpfValidationService;

@SpringBootTest
class FluxoViagemTests {
	@Autowired
	private CpfValidationService cpfValidationService;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private ReservaService reservaService;

	@Autowired
	private HotelService hotelService;

	@Autowired
	private PagamentoService pagamentoService;

	@Autowired
	private ClienteJpaRepository clienteJpaRepository;

	@Autowired
	private HotelJpaRepository hotelJpaRepository;

	@Autowired
	private PagamentoJpaRepository pagamentoJpaRepository;

	@Autowired
	private ReservaJpaRepository reservaJpaRepository;

	@BeforeEach
	void limparBancoDeTeste() {
		pagamentoJpaRepository.deleteAll();
		reservaJpaRepository.deleteAll();
		hotelJpaRepository.deleteAll();
		clienteJpaRepository.deleteAll();
	}

	@Test
	void validaCpfComABibliotecaFornecida() {
		assertTrue(cpfValidationService.isValid("529.982.247-25"));
		assertFalse(cpfValidationService.isValid("111.111.111-11"));
	}

	@Test
	void cadastraClienteComCpfValidoERejeitaDuplicidade() {
		Cliente cliente = clienteService.cadastrar("Ana Silva", "529.982.247-25", "ana@example.com");

		assertEquals("52998224725", cliente.cpf());
		assertThrows(DuplicateResourceException.class,
			() -> clienteService.cadastrar("Outra Pessoa", "52998224725", "outra@example.com"));
		assertThrows(InvalidRequestException.class,
			() -> clienteService.cadastrar("Pessoa Invalida", "11111111111", "invalida@example.com"));
	}

	@Test
	void reservaFicaPendenteEExigeClienteCadastrado() {
		Cliente cliente = clienteService.cadastrar("Ana Silva", "52998224725", "ana@example.com");
		Reserva reserva = reservaService.criar(cliente.id(), "Salvador", LocalDate.now().plusDays(10));

		assertEquals("PENDENTE_PAGAMENTO", reserva.status());
		assertEquals(1, reservaService.listarPorCliente(cliente.id()).size());
		assertThrows(ResourceNotFoundException.class,
			() -> reservaService.criar(UUID.randomUUID(), "Salvador", LocalDate.now().plusDays(10)));
	}

	@Test
	void cadastraHotelParceiroERejeitaDuplicadoNaMesmaCidade() {
		Hotel hotel = hotelService.cadastrar("Hotel Central", "Sao Paulo", 40);

		assertEquals(40, hotelService.buscar(hotel.id()).quartosDisponiveis());
		assertEquals(1, hotelService.listar().size());
		assertThrows(DuplicateResourceException.class,
			() -> hotelService.cadastrar("hotel central", "sao paulo", 12));
	}

	@Test
	void cartaoConfirmaReservaEboletoAguardaConfirmacaoManual() {
		Cliente cliente = clienteService.cadastrar("Ana Silva", "52998224725", "ana@example.com");
		Reserva reservaCartao = reservaService.criar(cliente.id(), "Recife", LocalDate.now().plusDays(12));
		Pagamento cartao = pagamentoService.registrar(
			reservaCartao.id(), new BigDecimal("1250.00"), Pagamento.FormaPagamento.CARTAO);

		assertEquals(Pagamento.Status.PAGO, cartao.status());
		assertEquals("CONFIRMADA", reservaService.buscar(reservaCartao.id()).status());

		Reserva reservaBoleto = reservaService.criar(cliente.id(), "Fortaleza", LocalDate.now().plusDays(20));
		Pagamento boleto = pagamentoService.registrar(
			reservaBoleto.id(), new BigDecimal("980.50"), Pagamento.FormaPagamento.BOLETO);

		assertEquals(Pagamento.Status.PENDENTE, boleto.status());
		assertEquals("PENDENTE_PAGAMENTO", reservaService.buscar(reservaBoleto.id()).status());

		Pagamento confirmado = pagamentoService.confirmarBoleto(boleto.id());
		assertEquals(Pagamento.Status.PAGO, confirmado.status());
		assertEquals("CONFIRMADA", reservaService.buscar(reservaBoleto.id()).status());
	}
}