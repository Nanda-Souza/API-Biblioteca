package com.db.api_biblioteca.domain.service;

import com.db.api_biblioteca.domain.dto.AutorRequest;
import com.db.api_biblioteca.domain.dto.AutorResponse;
import com.db.api_biblioteca.domain.dto.AutorUpdateRequest;
import com.db.api_biblioteca.domain.entity.Autor;
import com.db.api_biblioteca.domain.entity.Livro;
import com.db.api_biblioteca.domain.repository.AutorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AutorServiceTest {

    @Mock
    private AutorRepository autorRepository;

    @InjectMocks
    private AutorService autorService;

    @Test
    @DisplayName("Deve salvar e retornar o autor com sucesso!")
    void deveSalvarAutorComSucesso() {

        AutorRequest request = new AutorRequest(
                "Machado de Assis",
                "Masculino",
                "1839-06-21",
                "12345678900"
        );

        Autor autorSalvo = new Autor(
                request.nome(),
                LocalDate.parse(request.dataDeNascimento()),
                request.cpf()
        );
        autorSalvo.setSexo(request.sexo());

        when(autorRepository.save(any(Autor.class)))
                .thenReturn(autorSalvo);

        AutorResponse response = autorService.salvarAutor(request);

        assertNotNull(response, "O retorno não pode ser nulo!");
        assertEquals("Machado de Assis", response.nome(), "Deve retornar Machado de Assis!");
    }

    @Test
    @DisplayName("Não deve salvar o autor e deve retornar erro de o cpf já for cadastrado!")
    void naoDeveSalvarAutorComCpfJaCadastrado() {

        AutorRequest request = new AutorRequest(
                "Machado de Assis",
                "Masculino",
                "1839-06-21",
                "12345678900"
        );

        when(autorRepository.existsByCpf(request.cpf()))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> autorService.salvarAutor(request)
        );

        assertEquals("CPF já cadastrado!", exception.getMessage(), "Deve retornar a mensagem de cpf já cadastrado!");
        verify(autorRepository, never()).save(any());
    }

    @Test
    @DisplayName("Não deve salvar o autor e deve retornar erro de data de nascimento no formato inválido!")
    void naoDeveSalvarAutorComDataDeNascimentoNoFormatoInvalido() {

        AutorRequest request = new AutorRequest(
                "Machado de Assis",
                "Masculino",
                "18390621",
                "12345678900"
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> autorService.salvarAutor(request)
        );

        assertEquals("Data de nascimento no formato inválido ou no futuro! Favor fornecer data no formato aaaa-mm-dd!", exception.getMessage(), "Deve retornar a mensagem de data de nascimento errada!");
        verify(autorRepository, never()).save(any());
    }

    @Test
    @DisplayName("Não deve salvar o autor e deve retornar erro de sexo inválido!")
    void naoDeveSalvarAutorComSexoNoFormatoInvalido() {

        AutorRequest request = new AutorRequest(
                "Machado de Assis",
                "ValorInvalido",
                "1839-06-21",
                "12345678900"
        );

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> autorService.salvarAutor(request)
        );

        assertEquals("Sexo inválido. Valores permitidos: Masculino, Feminino ou Outro!", exception.getMessage(), "Deve retornar a mensagem de Sexo inválido!");
        verify(autorRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve listar todos os autores cadastrados!")
    void deveListarAutoresComSucesso() {

        Autor autor1 = new Autor(
                "Machado de Assis",
                LocalDate.parse("1839-06-21"),
                "12345678900"
        );
        autor1.setSexo("Masculino");

        Autor autor2 = new Autor(
                "Carlos Drummond de Andrade",
                LocalDate.parse("1902-10-31"),
                "98765432100"
        );
        autor2.setSexo("Masculino");

        when(autorRepository.findAll())
                .thenReturn(List.of(autor1, autor2));

        List<AutorResponse> response = autorService.listarAutores();

        assertNotNull(response, "O retorno não pode ser nulo!");
        assertEquals(2, response.size(), "Deve retornar uma lista com dois autores!");

        AutorResponse primeiroAutor = response.get(0);
        assertEquals("Machado de Assis", primeiroAutor.nome(), "Deve retornar Machado de Assis!");

        AutorResponse segundoAutor = response.get(1);
        assertEquals("Carlos Drummond de Andrade", segundoAutor.nome(), "Carlos Drummond de Andrade!");

        verify(autorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve buscar autor por nome completo com sucesso!")
    void deveBuscarAutorPorNomeCompletoComSucesso() {

        String nome = "Machado de Assis";

        Autor autor = new Autor(
                nome,
                LocalDate.parse("1839-06-21"),
                "12345678900"
        );
        autor.setSexo("Masculino");

        when(autorRepository.findByNomeIgnoreCase(nome))
                .thenReturn(Optional.of(autor));

        AutorResponse response = autorService.buscarAutorPorNomeCompleto(nome);

        assertNotNull(response, "O retorno não pode ser nulo!");
        assertEquals(nome, response.nome(), "Deve retornar Machado de Assis!");

        verify(autorRepository).findByNomeIgnoreCase(nome);
    }

    @Test
    @DisplayName("Deve lançar exceção quando autor não for encontrado!")
    void deveLancarExcecaoQuandoDeletarAutorNaoEncontrado() {

        String nome = "abcde";

        when(autorRepository.findByNomeIgnoreCase(nome))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> autorService.buscarAutorPorNomeCompleto(nome)
        );

        assertEquals("Autor abcde não encontrado", exception.getMessage(), "Deve retornar autor abdce não encontrado!");

        verify(autorRepository).findByNomeIgnoreCase(nome);
    }

    @Test
    @DisplayName("Deve deletar autor com sucesso!")
    void deveDeletarAutorComSucesso() {

        Long id = 1L;

        Autor autor = new Autor(
                "Machado de Assis",
                LocalDate.parse("1839-06-21"),
                "12345678900"
        );

        when(autorRepository.findById(id))
                .thenReturn(Optional.of(autor));

        autorService.deletarAutor(id);

        verify(autorRepository).findById(id);
        verify(autorRepository).delete(autor);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar autor inexistente!")
    void deveLancarExcecaoAoDeletarAutorInexistente() {

        Long id = 1L;

        when(autorRepository.findById(id))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> autorService.deletarAutor(id)
        );

        assertEquals("Autor com Id 1 não encontrado!",exception.getMessage(), "Deve retornar autor com id 1 não encontrado!");

        verify(autorRepository).findById(id);
        verify(autorRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Não deve excluir autor que possua livros associados!")
    void naoDeveExcluirAutorComLivros() {

        Long autorId = 1L;

        Autor autor = new Autor(
                "Jorge Amado",
                LocalDate.parse("1912-08-10"),
                "12345678900"
        );

        Livro livro = new Livro(
                "Capitães da Areia",
                "9788535914849",
                LocalDate.parse("1937-01-01")
        );

        autor.getLivros().add(livro);

        when(autorRepository.findById(autorId))
                .thenReturn(Optional.of(autor));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> autorService.deletarAutor(autorId)
        );

        assertEquals("Não é possível excluir um autor que tenha livros associados!", exception.getMessage(), "Deve retornar que não é possivel excluir pois autor tem um livro associado!");

        verify(autorRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Deve atualizar o autor com sucesso!")
    void deveAtualizarAutorComSucesso() {

        Long id = 1L;

        Autor autor = new Autor(
                "Machado",
                LocalDate.parse("1839-06-21"),
                "12345678900"
        );
        autor.setSexo("Masculino");

        AutorUpdateRequest updateRequest = new AutorUpdateRequest(
                "Machado de Assis",
                null,
                null,
                null
        );

        when(autorRepository.findById(id))
                .thenReturn(Optional.of(autor));

        when(autorRepository.save(any(Autor.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AutorResponse response = autorService.atualizarAutor(id, updateRequest);

        assertNotNull(response, "O retorno não pode ser nulo!");
        assertEquals("Machado de Assis", response.nome(), "Deve retornar Machado de Assis!");

        verify(autorRepository).findById(id);
        verify(autorRepository).save(any(Autor.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar autor inexistente")
    void deveLancarExcecaoQuandoAtualizarAutorNaoEncontrado() {

        Long id = 1L;

        AutorUpdateRequest updateRequest = new AutorUpdateRequest(
                "Machado de Assis",
                null,
                null,
                null
        );

        when(autorRepository.findById(id))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> autorService.atualizarAutor(id, updateRequest)
        );

        assertEquals("Autor com Id 1 não encontrado!",exception.getMessage(), "Deve retornar autor com id 1 não encontrado!");

        verify(autorRepository).findById(id);
        verify(autorRepository, never()).save(any());
    }

    @Test
    @DisplayName("Não deve atualizar autor quando CPF já estiver cadastrado!")
    void naoDeveAtualizarAutorComCpfDuplicado() {

        Long id = 1L;

        Autor autorExistente = new Autor(
                "Machado de Assis",
                LocalDate.parse("1839-06-21"),
                "12345678900"
        );

        AutorUpdateRequest updateRequest = new AutorUpdateRequest(
                null,
                null,
                null,
                "00987654321"
        );

        when(autorRepository.findById(id))
                .thenReturn(Optional.of(autorExistente));

        when(autorRepository.existsByCpf("00987654321"))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> autorService.atualizarAutor(id, updateRequest)
        );

        assertEquals("CPF já cadastrado!", exception.getMessage(), "Deve retornar a mensagem de cpf já cadastrado!");

        verify(autorRepository).findById(id);
        verify(autorRepository, never()).save(any());
    }

}
