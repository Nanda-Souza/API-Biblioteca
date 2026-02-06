package com.db.api_biblioteca.domain.service;

import com.db.api_biblioteca.domain.dto.AluguelResponse;
import com.db.api_biblioteca.domain.dto.LivroResponse;
import com.db.api_biblioteca.domain.entity.Aluguel;
import com.db.api_biblioteca.domain.entity.Autor;
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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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



}
