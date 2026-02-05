package com.db.api_biblioteca.domain.service;

import com.db.api_biblioteca.domain.dto.LocatarioRequest;
import com.db.api_biblioteca.domain.dto.LocatarioResponse;
import com.db.api_biblioteca.domain.entity.Locatario;
import com.db.api_biblioteca.domain.repository.LocatarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocatarioServiceTest {

    @Mock
    private LocatarioRepository locatarioRepository;

    @InjectMocks
    private LocatarioService locatarioService;

    @Test
    @DisplayName("Deve salvar e retornar o locatário com sucesso!")
    void deveSalvarLocatarioComSucesso() {

        LocatarioRequest request = new LocatarioRequest(
                "Ana Paula Souza",
                "Feminino",
                "987654321",
                "ana.souza@email.com",
                "1990-05-12",
                "12345678901"
        );

        Locatario locatarioSalvo = new Locatario(
                request.nome(),
                request.telefone(),
                request.email(),
                LocalDate.parse(request.dataDeNascimento()),
                request.cpf()
        );
        locatarioSalvo.setSexo(request.sexo());

        when(locatarioRepository.save(any(Locatario.class)))
                .thenReturn(locatarioSalvo);

        LocatarioResponse response = locatarioService.salvarLocatario(request);

        assertNotNull(response, "O retorno não pode ser nulo!");
        assertEquals("Ana Paula Souza", response.nome(), "Deve retornar Ana Paula Souza!");
    }

    @Test
    @DisplayName("Não deve salvar o locatário e deve retornar erro de o cpf já for cadastrado!")
    void naoDeveSalvarLocatarioComCpfJaCadastrado() {

        LocatarioRequest request = new LocatarioRequest(
                "Ana Paula Souza",
                "Feminino",
                "987654321",
                "ana.souza@email.com",
                "1990-05-12",
                "12345678901"
        );

        when(locatarioRepository.existsByCpf(request.cpf()))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> locatarioService.salvarLocatario(request)
        );

        assertEquals("CPF já cadastrado!", exception.getMessage(), "Deve retornar a mensagem de cpf já cadastrado!");
        verify(locatarioRepository, never()).save(any());

    }

    @Test
    @DisplayName("Não deve salvar o locatário e deve retornar erro de o email já for cadastrado!")
    void naoDeveSalvarLocatarioComEmailJaCadastrado() {

        LocatarioRequest request = new LocatarioRequest(
                "Ana Paula Souza",
                "Feminino",
                "987654321",
                "ana.souza@email.com",
                "1990-05-12",
                "12345678901"
        );

        when(locatarioRepository.existsByEmail(request.email()))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> locatarioService.salvarLocatario(request)
        );

        assertEquals("Email já cadastrado!", exception.getMessage(), "Deve retornar a mensagem de email já cadastrado!");
        verify(locatarioRepository, never()).save(any());

    }

    @Test
    @DisplayName("Não deve salvar o locatário e deve retornar erro de data de nascimento no formato inválido!")
    void naoDeveSalvarLocatarioComDataDeNascimentoNoFormatoInvalido() {

        LocatarioRequest request = new LocatarioRequest(
                "Ana Paula Souza",
                "Feminino",
                "987654321",
                "ana.souza@email.com",
                "199005-12",
                "12345678901"
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> locatarioService.salvarLocatario(request)
        );

        assertEquals("Data de nascimento no formato inválido ou no futuro! Favor fornecer data no formato aaaa-mm-dd!", exception.getMessage(), "Deve retornar a mensagem de data de nascimento errada!");
        verify(locatarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Não deve salvar o locatário e deve retornar erro de sexo inválido!")
    void naoDeveSalvarAutorComSexoNoFormatoInvalido() {

        LocatarioRequest request = new LocatarioRequest(
                "Ana Paula Souza",
                "Valor Inválido",
                "987654321",
                "ana.souza@email.com",
                "1990-05-12",
                "12345678901"
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> locatarioService.salvarLocatario(request)
        );

        assertEquals("Sexo inválido. Valores permitidos: Masculino, Feminino ou Outro!", exception.getMessage(), "Deve retornar a mensagem de Sexo inválido!");
        verify(locatarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve listar todos os locatários cadastrados!")
    void deveListarLocatariosComSucesso() {

        Locatario locatario1 = new Locatario(
                "Ana Paula Souza",
                "987654321",
                "ana.souza@email.com",
                LocalDate.parse("1990-05-12"),
                "12345678901"
        );

        locatario1.setSexo("Feminino");

        Locatario locatario2 = new Locatario(
                "Fernanda Alvez",
                "987654321",
                "fernanda.alves@email.com",
                LocalDate.parse("1993-02-03"),
                "34567890123"
        );

        locatario2.setSexo("Feminino");

        when(locatarioRepository.findAll())
                .thenReturn(List.of(locatario1, locatario2));

        List<LocatarioResponse> response = locatarioService.listarLocatarios();

        assertNotNull(response, "O retorno não pode ser nulo!");
        assertEquals(2, response.size(), "Deve retornar uma lista com dois locatários!");

        LocatarioResponse primeiroLocatario = response.get(0);
        assertEquals("Ana Paula Souza", primeiroLocatario.nome(), "Deve retornar Ana Paula Souza!");

        LocatarioResponse segundoLocatario = response.get(1);
        assertEquals("Fernanda Alvez", segundoLocatario.nome(), "Deve retornar Fernanda Alvez!");

        verify(locatarioRepository, times(1)).findAll();
    }


}
