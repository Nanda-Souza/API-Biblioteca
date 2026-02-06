package com.db.api_biblioteca.domain.service;

import com.db.api_biblioteca.domain.dto.AluguelRequest;
import com.db.api_biblioteca.domain.dto.AluguelResponse;
import com.db.api_biblioteca.domain.dto.LivroResponse;
import com.db.api_biblioteca.domain.entity.Aluguel;
import com.db.api_biblioteca.domain.entity.Livro;
import com.db.api_biblioteca.domain.entity.Locatario;
import com.db.api_biblioteca.domain.repository.AluguelRepository;
import com.db.api_biblioteca.domain.repository.LivroRepository;
import com.db.api_biblioteca.domain.repository.LocatarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AluguelServiceTest {

    @Mock
    private AluguelRepository aluguelRepository;

    @Mock
    private LocatarioRepository locatarioRepository;

    @Mock
    private LivroRepository livroRepository;

    @InjectMocks
    private AluguelService aluguelService;

    private void setId(Object entity, Long id) {
        try {
            Field field = entity.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(entity, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Deve listar todos os alugueis cadastrados!")
    void deveListarAlugueisComSucesso() {

        Locatario locatario = new Locatario(
                "Ana Paula Souza Oliveira",
                "987654321",
                "ana.souza.oliveira.@email.com",
                LocalDate.parse("1990-05-12"),
                "12345678901"
        );

        Livro livro1 = new Livro(
                "Laços de Família",
                "9788535920383",
                LocalDate.parse("1960-01-01")
        );

        Livro livro2 = new Livro(
                "Perto do Coração Selvagem",
                "9788535920413",
                LocalDate.parse("1943-01-01")
        );

        List<Livro> livros = List.of(livro1, livro2);

        Aluguel aluguel = new Aluguel(locatario, livros);

        when(aluguelRepository.findAll())
                .thenReturn(List.of(aluguel));

        List<AluguelResponse> response = aluguelService.listarAlugueis();

        assertNotNull(response, "O retorno não pode ser nulo!");
        assertEquals(1, response.size(), "Deve retornar uma lista com um aluguel!");

        AluguelResponse aluguelResponse = response.get(0);
        assertEquals(2, aluguelResponse.livrosIds().size(), "Deve retornar dois livros!");
        assertNotNull(aluguelResponse.dataRetirada(), "Data de retirada não pode ser nula!");
        assertNotNull(aluguelResponse.dataDevolucao(), "Data de devolução não pode ser nula!");

        verify(aluguelRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve salvar aluguel com sucesso!")
    void deveSalvarAluguelComSucesso() {

        Locatario locatario = new Locatario(
                "Ana Paula Souza Oliveira",
                "987654321",
                "ana.souza.oliveira@email.com",
                LocalDate.parse("1990-05-12"),
                "12345678901"
        );

        ReflectionTestUtils.setField(locatario, "id", 1L);

        Livro livro1 = new Livro(
                "Laços de Família",
                "9788535920383",
                LocalDate.parse("1960-01-01")
        );

        Livro livro2 = new Livro(
                "Perto do Coração Selvagem",
                "9788535920413",
                LocalDate.parse("1943-01-01")
        );

        List<Long> livrosIds = List.of(1L, 2L);
        AluguelRequest request = new AluguelRequest(1L, livrosIds);

        when(locatarioRepository.findById(1L))
                .thenReturn(Optional.of(locatario));

        when(livroRepository.findAllById(livrosIds))
                .thenReturn(List.of(livro1, livro2));

        when(aluguelRepository.save(any(Aluguel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AluguelResponse response = aluguelService.salvarAluguel(request);

        assertNotNull(response, "O retorno não pode ser nulo!");
        assertNotNull(response.dataRetirada(), "Data de retirada não pode ser nula!");
        assertNotNull(response.dataDevolucao(), "Data de devolução não pode ser nula!");
        assertEquals(2, response.livrosIds().size(), "Deve retornar dois livros!");
        assertEquals(1L, response.locatarioId(), "Deve retornar o id do locatário!");

        verify(locatarioRepository, times(1)).findById(1L);
        verify(livroRepository, times(1)).findAllById(livrosIds);
        verify(aluguelRepository, times(1)).save(any(Aluguel.class));
    }

    @Test
    @DisplayName("Deve lançar erro quando locatário não for encontrado!")
    void deveLancarErroQuandoLocatarioNaoExistir() {

        AluguelRequest request = new AluguelRequest(
                1L,
                List.of(1L)
        );

        when(locatarioRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> aluguelService.salvarAluguel(request)
        );

        assertEquals(
                "Locatário com id 1 não encontrado!",
                exception.getMessage(),
                "Mensagem de erro deve ser correta!"
        );

        verify(locatarioRepository, times(1)).findById(1L);
        verify(livroRepository, never()).findAllById(any());
        verify(aluguelRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar erro quando algum livro não for encontrado!")
    void deveLancarErroQuandoLivroNaoForEncontrado() {

        Locatario locatario = new Locatario(
                "Carlos Eduardo",
                "987654321",
                "carlos@email.com",
                LocalDate.parse("1985-03-20"),
                "98765432100"
        );

        AluguelRequest request = new AluguelRequest(
                1L,
                List.of(1L, 2L)
        );

        Livro livro = new Livro(
                "Laços de Família",
                "9788535920383",
                LocalDate.parse("1960-01-01")
        );

        when(locatarioRepository.findById(1L))
                .thenReturn(Optional.of(locatario));

        when(livroRepository.findAllById(List.of(1L, 2L)))
                .thenReturn(List.of(livro));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> aluguelService.salvarAluguel(request)
        );

        assertEquals(
                "Um ou mais livros não foram encontrados!",
                exception.getMessage(),
                "Mensagem de erro deve ser correta!"
        );

        verify(locatarioRepository, times(1)).findById(1L);
        verify(livroRepository, times(1)).findAllById(List.of(1L, 2L));
        verify(aluguelRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve listar apenas livros disponíveis para aluguel")
    void deveListarLivrosDisponiveisParaAluguel() {

        Livro livroDisponivel = new Livro(
                "Capitães da Areia",
                "9788535914849",
                LocalDate.parse("1937-01-01")
        );

        Livro livroAlugado = new Livro(
                "Gabriela, Cravo e Canela",
                "9788535914856",
                LocalDate.parse("1958-01-01")
        );

        // 👇 simula IDs gerados pelo banco
        setId(livroDisponivel, 1L);
        setId(livroAlugado, 2L);

        when(livroRepository.findAll())
                .thenReturn(List.of(livroDisponivel, livroAlugado));

        when(aluguelRepository.buscarIdsLivrosAlugados())
                .thenReturn(List.of(2L));

        List<LivroResponse> response =
                aluguelService.listarLivrosDisponiveis();

        assertEquals(1, response.size(), "Deve retornar uma lista com um livro disponivel!");
        assertEquals("Capitães da Areia", response.get(0).nome(), "Deve retornar Capitães da Areia!");
    }




}
