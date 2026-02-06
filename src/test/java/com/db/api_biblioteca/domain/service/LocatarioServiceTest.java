package com.db.api_biblioteca.domain.service;

import com.db.api_biblioteca.domain.dto.LocatarioRequest;
import com.db.api_biblioteca.domain.dto.LocatarioResponse;
import com.db.api_biblioteca.domain.dto.LocatarioUpdateRequest;
import com.db.api_biblioteca.domain.entity.Locatario;
import com.db.api_biblioteca.domain.repository.AluguelRepository;
import com.db.api_biblioteca.domain.repository.LocatarioRepository;
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
public class LocatarioServiceTest {

    @Mock
    private LocatarioRepository locatarioRepository;

    @Mock
    private AluguelRepository aluguelRepository;

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

    @Test
    @DisplayName("Deve atualizar locatário com sucesso!")
    void deveAtualizarLocatarioComSucesso() {

        Long id = 1L;

        Locatario locatario = new Locatario(
                "Ana",
                "993569926",
                "ana@gmail.com",
                LocalDate.parse("1990-05-05"),
                "12345678900"

        );

        LocatarioUpdateRequest updateRequest = new LocatarioUpdateRequest(
                "Ana Paula",
                null,
                null,
                null,
                null,
                null
        );

        when(locatarioRepository.findById(id))
                .thenReturn(Optional.of(locatario));

        when(locatarioRepository.save(any(Locatario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LocatarioResponse response = locatarioService.atualizarLocatario(id, updateRequest);

        assertNotNull(response, "O retorno não pode ser nulo!");
        assertEquals("Ana Paula", response.nome(), "Deve retornar Ana Paula!");

        verify(locatarioRepository).findById(id);
        verify(locatarioRepository).save(any(Locatario.class));

    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar locatário inexistente")
    void deveLancarExcecaoQuandoAtualizarLocatarioNaoEncontrado() {

        Long id = 1L;

        LocatarioUpdateRequest updateRequest = new LocatarioUpdateRequest(
                "Ana Paula",
                null,
                null,
                null,
                null,
                null
        );

        when(locatarioRepository.findById(id))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> locatarioService.atualizarLocatario(id, updateRequest)
        );

        assertEquals("Locatário com Id 1 não encontrado!",exception.getMessage(), "Deve retornar locatário com id 1 não encontrado!");

        verify(locatarioRepository).findById(id);
        verify(locatarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Não deve atualizar locatário quando CPF já estiver cadastrado!")
    void naoDeveAtualizarLocatarioComCpfDuplicado() {

        Long id = 1L;

        Locatario locatarioExistente = new Locatario(
                "Ana",
                "993569926",
                "ana@gmail.com",
                LocalDate.parse("1990-05-05"),
                "12345678900"

        );

        LocatarioUpdateRequest updateRequest = new LocatarioUpdateRequest(
                null,
                null,
                null,
                null,
                null,
                "12345678900"
        );

        when(locatarioRepository.findById(id))
                .thenReturn(Optional.of(locatarioExistente));

        when(locatarioRepository.existsByCpf("12345678900"))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> locatarioService.atualizarLocatario(id, updateRequest)
        );

        assertEquals("CPF já cadastrado!", exception.getMessage(), "Deve retornar a mensagem de cpf já cadastrado!");

        verify(locatarioRepository).findById(id);
        verify(locatarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Não deve atualizar locatário quando Email já estiver cadastrado!")
    void naoDeveAtualizarLocatarioComEmailDuplicado() {

        Long id = 1L;

        Locatario locatarioExistente = new Locatario(
                "Ana",
                "993569926",
                "ana@gmail.com",
                LocalDate.parse("1990-05-05"),
                "12345678900"

        );

        LocatarioUpdateRequest updateRequest = new LocatarioUpdateRequest(
                null,
                null,
                null,
                "ana@gmail.com",
                null,
                null
        );

        when(locatarioRepository.findById(id))
                .thenReturn(Optional.of(locatarioExistente));

        when(locatarioRepository.existsByEmail("ana@gmail.com"))
                .thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> locatarioService.atualizarLocatario(id, updateRequest)
        );

        assertEquals("Email já cadastrado!", exception.getMessage(), "Deve retornar a mensagem de email já cadastrado!");

        verify(locatarioRepository).findById(id);
        verify(locatarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve deletar locatárop com sucesso!")
    void deveDeletarLocatarioComSucesso() {

        Long id = 1L;

        Locatario locatario = new Locatario(
                "Ana",
                "993569926",
                "ana@gmail.com",
                LocalDate.parse("1990-05-05"),
                "12345678900"

        );

        when(locatarioRepository.findById(id))
                .thenReturn(Optional.of(locatario));

        locatarioService.deletarLocatario(id);

        verify(locatarioRepository).findById(id);
        verify(locatarioRepository).delete(locatario);

    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar locatário inexistente!")
    void deveLancarExcecaoAoDeletarLocatarioInexistente() {

        Long id = 1L;

        when(locatarioRepository.findById(id))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> locatarioService.deletarLocatario(id)
        );

        assertEquals("Locatário com Id 1 não encontrado!",exception.getMessage(), "Deve retornar locatário com id 1 não encontrado!");

        verify(locatarioRepository).findById(id);
        verify(locatarioRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Não deve deletar locatário quando ele possuir livro para devolução!")
    void naoDeveDeletarLocatarioQuandoPossuirLivroParaDevolucao() {

        Locatario locatario = new Locatario(
                "Ana Paula Souza Oliveira",
                "987654321",
                "ana.souza@email.com",
                LocalDate.parse("1990-05-12"),
                "12345678901"
        );

        when(locatarioRepository.findById(1L))
                .thenReturn(Optional.of(locatario));

        when(aluguelRepository.existsByLocatario_Id(1L))
                .thenReturn(true);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> locatarioService.deletarLocatario(1L)
        );

        assertEquals("Locatário não pode ser excluído pois possui livro(s) para devolver!",exception.getMessage(),"Deve impedir exclusão do locatário com aluguel ativo!");

        verify(locatarioRepository, never()).delete(any(Locatario.class));
    }


}
