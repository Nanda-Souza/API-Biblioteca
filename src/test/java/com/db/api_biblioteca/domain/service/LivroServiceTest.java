package com.db.api_biblioteca.domain.service;

import com.db.api_biblioteca.domain.dto.AutorRequest;
import com.db.api_biblioteca.domain.dto.AutorResponse;
import com.db.api_biblioteca.domain.dto.LivroRequest;
import com.db.api_biblioteca.domain.dto.LivroResponse;
import com.db.api_biblioteca.domain.entity.Autor;
import com.db.api_biblioteca.domain.entity.Livro;
import com.db.api_biblioteca.domain.repository.AutorRepository;
import com.db.api_biblioteca.domain.repository.LivroRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LivroServiceTest {

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private AutorRepository autorRepository;

    @InjectMocks
    private LivroService livroService;

    @Test
    @DisplayName("Deve salvar e retornar livro com sucesso quando existir ao menos um autor!")
    void deveSalvarLivroComSucesso() {

        LivroRequest request = new LivroRequest(
                "Capitães da Areia",
                "9788535914849",
                "1937-01-01",
                List.of(2L)
        );

        Autor autor = new Autor(
                "Jorge Amado",
                LocalDate.parse("1912-08-10"),
                "12345678900"
        );
        autor.setSexo("Masculino");

        when(autorRepository.findAllById(List.of(2L)))
                .thenReturn(List.of(autor));

        when(livroRepository.save(any(Livro.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LivroResponse response = livroService.salvarLivro(request);

        assertNotNull(response, "O retorno não pode ser nulo!");
        assertEquals("Capitães da Areia", response.nome(), "Deve retornar o livro Capitães da Areia!");

        verify(autorRepository).findAllById(List.of(2L));
        verify(livroRepository).save(any(Livro.class));
    }

    @Test
    @DisplayName("Não deve salvar livro quando um ou mais autores não forem encontrados!")
    void naoDeveSalvarLivroQuandoAutorNaoExiste() {

        LivroRequest request = new LivroRequest(
                "Capitães da Areia",
                "9788535914849",
                "1937-01-01",
                List.of(2L, 99L)
        );

        Autor autorExistente = new Autor(
                "Jorge Amado",
                LocalDate.parse("1912-08-10"),
                "12345678900"
        );
        autorExistente.setSexo("Masculino");

        when(autorRepository.findAllById(request.autoresIds()))
                .thenReturn(List.of(autorExistente));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> livroService.salvarLivro(request)
        );

        assertEquals("Um ou mais autores não encontrados!", exception.getMessage(),"Deve retornar erro quando nem todos os autores existirem!");

        verify(livroRepository, never()).save(any(Livro.class));
    }

    @Test
    @DisplayName("Não deve salvar o livro e deve retornar erro se o isbd já for cadastrado!")
    void naoDeveSalvarLivroComIsbdJaCadastrado() {

        LivroRequest request = new LivroRequest(
                "Capitães da Areia",
                "9788535914849",
                "1937-01-01",
                List.of(2L)
        );

        Autor autor = new Autor(
                "Jorge Amado",
                LocalDate.parse("1912-08-10"),
                "12345678900"
        );
        autor.setSexo("Masculino");

        when(livroRepository.existsByIsbd(request.isbd()))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> livroService.salvarLivro(request)
        );

        assertEquals("ISBD já cadastrado!", exception.getMessage(), "Deve retornar a mensagem de isbd já cadastrado!");
        verify(autorRepository, never()).save(any());
    }

    @Test
    @DisplayName("Não deve salvar o autor e deve retornar erro de data de publicação no formato inválido!")
    void naoDeveSalvarLivroComDataDePublicacaoNoFormatoInvalido() {

        LivroRequest request = new LivroRequest(
                "Capitães da Areia",
                "9788535914849",
                "19370101",
                List.of(2L)
        );

        Autor autor = new Autor(
                "Jorge Amado",
                LocalDate.parse("1912-08-10"),
                "12345678900"
        );
        autor.setSexo("Masculino");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> livroService.salvarLivro(request)
        );

        assertEquals("Data de publicação no formato inválido! Favor fornecer data no formato aaaa-mm-dd!", exception.getMessage(), "Deve retornar a mensagem de data de publicação errada!");
        verify(autorRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve listar todos os livros cadastrados!")
    void deveListarLivrosComSucesso() {

        Autor autor = new Autor(
                "Jorge Amado",
                LocalDate.parse("1912-08-10"),
                "12345678900"
        );
        autor.setSexo("Masculino");

        Livro livro1 = new Livro(
                "Capitães da Areia",
                "9788535914849",
                LocalDate.parse("1937-01-01")
        );

        Livro livro2 = new Livro(
                "Gabriela, Cravo e Canela",
                "9788535914856",
                LocalDate.parse("1958-01-01")
        );

        livro1.getAutores().add(autor);
        livro2.getAutores().add(autor);

        when(livroRepository.findAll())
                .thenReturn(List.of(livro1, livro2));

        List<LivroResponse> response = livroService.listarLivros();

        assertNotNull(response, "O retorno não pode ser nulo!");
        assertEquals(2, response.size(), "Deve retornar uma lista com dois livros!");

        LivroResponse primeiroLivro = response.get(0);
        assertEquals("Capitães da Areia", primeiroLivro.nome(), "Deve retornar Capitães da Areia!");
        assertEquals(1, primeiroLivro.autoresIds().size(), "Deve retornar a lista de ids de autores com tamanho 1!");

        LivroResponse segundoLivro = response.get(1);
        assertEquals("Gabriela, Cravo e Canela", segundoLivro.nome(), "Deve retornar Gabriela, Cravo e Canela!");
        assertEquals(1, segundoLivro.autoresIds().size(), "Deve retornar a  lista de ids de autores com tamanho 1!");

        verify(livroRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve adicionar autor ao livro com sucesso!")
    void deveAdicionarAutorAoLivroComSucesso() {

        Livro livro = new Livro(
                "Capitães da Areia",
                "9788535914849",
                LocalDate.parse("1937-01-01")
        );

        Autor autor = new Autor(
                "Jorge Amado",
                LocalDate.parse("1912-08-10"),
                "12345678900"
        );

        when(livroRepository.findById(1L))
                .thenReturn(Optional.of(livro));

        when(autorRepository.findById(2L))
                .thenReturn(Optional.of(autor));

        when(livroRepository.save(any(Livro.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LivroResponse response = livroService.adicionarAutorAoLivro(1L, 2L);

        assertNotNull(response, "O retorno não pode ser nulo!");
        assertEquals("Capitães da Areia", response.nome(), "Deve retornar Capitães da Areia!");

        verify(livroRepository).save(livro);
    }

    @Test
    @DisplayName("Deve lançar erro quando o livro não existir!")
    void naoDeveAssociarLivroAoAutorQuandoNaoEncontrarIdDoLivro() {

        when(livroRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> livroService.adicionarAutorAoLivro(1L, 2L)
        );

        assertEquals("Livro com id 1 não encontrado!", exception.getMessage(), "Deve retornar livro com id 1 não encontrado!");

        verify(livroRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar erro quando o autor não existir!")
    void naoDeveAssociarLivroAoAutorQuandoNaoEncontrarIdDoAutor() {

        Livro livro = new Livro(
                "Capitães da Areia",
                "9788535914849",
                LocalDate.parse("1937-01-01")
        );

        when(livroRepository.findById(1L))
                .thenReturn(Optional.of(livro));

        when(autorRepository.findById(2L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> livroService.adicionarAutorAoLivro(1L, 2L)
        );

        assertEquals("Autor com id 2 não encontrado!", exception.getMessage(), "Deve retornar livro com id 1 não encontrado!");

        verify(livroRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar erro quando autor já estiver associado ao livro!")
    void naoDeveAssociarLivroAoAutorQuandoIdDoAutorJaEstiverAssociado() {

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

        livro.getAutores().add(autor);

        when(livroRepository.findById(1L))
                .thenReturn(Optional.of(livro));

        when(autorRepository.findById(2L))
                .thenReturn(Optional.of(autor));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> livroService.adicionarAutorAoLivro(1L, 2L)
        );

        assertEquals("Autor já está associado a este livro!", exception.getMessage(), "Deve retornar que o autor já está associado ao livro!");

        verify(livroRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve remover autor do livro com sucesso!")
    void deveRemoverAutorDoLivroComSucesso() {

        Autor autor1 = new Autor(
                "Jorge Amado",
                LocalDate.parse("1912-08-10"),
                "12345678900"
        );

        Autor autor2 = new Autor(
                "Machado de Assis",
                LocalDate.parse("1839-06-21"),
                "12345678902"
        );

        Livro livro = new Livro(
                "Capitães da Areia",
                "9788535914849",
                LocalDate.parse("1937-01-01")
        );

        livro.getAutores().add(autor1);
        livro.getAutores().add(autor2);

        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
        when(autorRepository.findById(2L)).thenReturn(Optional.of(autor2));
        when(livroRepository.save(any(Livro.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LivroResponse response = livroService.removerAutorDoLivro(1L, 2L);

        assertNotNull(response, "O retorno não pode ser nulo!");
        assertEquals(1, response.autoresIds().size(), "Deve retornar uma lista com apenas 1 id de autor!");
        assertFalse(response.autoresIds().contains(2L), "Deve retornar o id de autor numero 2!");

        verify(livroRepository).save(livro);
    }

    @Test
    @DisplayName("Deve lançar exceção quando livro não existir!")
    void naoDeveRemoverAutorDoLivroQuandoLivroNaoExiste() {

        when(livroRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> livroService.removerAutorDoLivro(1L, 2L)
        );

        assertEquals("Livro com id 1 não encontrado!",exception.getMessage(), "Deve retornar livro com id 1 não encontrado!");

        verify(livroRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando autor não existir!")
    void naoDeveRemoverAutorDoLivroQuandoAutorNaoExiste() {

        Livro livro = new Livro(
                "Capitães da Areia",
                "9788535914849",
                LocalDate.parse("1937-01-01")
        );

        when(livroRepository.findById(1L))
                .thenReturn(Optional.of(livro));

        when(autorRepository.findById(2L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> livroService.removerAutorDoLivro(1L, 2L)
        );

        assertEquals("Autor com id 2 não encontrado!", exception.getMessage(), "Deve retornar autor com id 2 não encontrado!");

        verify(livroRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando autor não estiver associado ao livro!")
    void naoDeveRemoverAutorDoLivroQuandoAutorNaoAssociado() {

        Autor autorAssociado = new Autor(
                "Jorge Amado",
                LocalDate.parse("1912-08-10"),
                "12345678900"
        );

        Autor autorNaoAssociado = new Autor(
                "Machado de Assis",
                LocalDate.parse("1839-06-21"),
                "12345678902"
        );

        Livro livro = new Livro(
                "Capitães da Areia",
                "9788535914849",
                LocalDate.parse("1937-01-01")
        );

        livro.getAutores().add(autorAssociado);

        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
        when(autorRepository.findById(2L)).thenReturn(Optional.of(autorNaoAssociado));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> livroService.removerAutorDoLivro(1L, 2L)
        );

        assertEquals("Autor não está associado a este livro!",exception.getMessage(), "Deve retornar que o autor não está associado ao livro!");

        verify(livroRepository, never()).save(any());
    }

    @Test
    @DisplayName("Não deve permitir remover o último autor do livro!")
    void naoDeveRemoverUltimoAutorDoLivro() {

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

        livro.getAutores().add(autor);

        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
        when(autorRepository.findById(2L)).thenReturn(Optional.of(autor));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> livroService.removerAutorDoLivro(1L, 2L)
        );

        assertEquals("Um livro não pode ficar sem autor, favor excluir o livro!", exception.getMessage(), "Deve retornar que o livro não pode ficar sem autor!");

        verify(livroRepository, never()).save(any());
    }


}
