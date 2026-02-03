package com.db.api_biblioteca.domain.service;

import com.db.api_biblioteca.domain.dto.AutorRequest;
import com.db.api_biblioteca.domain.dto.AutorResponse;
import com.db.api_biblioteca.domain.dto.AutorUpdateRequest;
import com.db.api_biblioteca.domain.entity.Autor;
import com.db.api_biblioteca.domain.entity.Livro;
import com.db.api_biblioteca.domain.repository.AutorRepository;
import com.db.api_biblioteca.domain.validation.DataValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AutorService {

    private final AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public List<AutorResponse> listarAutores() {

        return autorRepository.findAll()
                .stream()
                .map(autor -> new AutorResponse(
                        autor.getId(),
                        autor.getNome(),
                        autor.getSexo(),
                        autor.getDataDeNascimento().toString(),
                        autor.getCpf(),
                        autor.getLivros()
                                .stream()
                                .map(Livro::getId)
                                .toList()
                ))
                .toList();
    }

    public AutorResponse buscarAutorPorNomeCompleto(String nome) {
        Autor autor = autorRepository.findByNomeIgnoreCase(nome)
                .orElseThrow(() ->
                        new RuntimeException("Autor " + nome + " não encontrado")
                );

        return new AutorResponse(
                autor.getId(),
                autor.getNome(),
                autor.getSexo(),
                autor.getDataDeNascimento().toString(),
                autor.getCpf(),
                autor.getLivros()
                        .stream()
                        .map(Livro::getId)
                        .toList()
        );
    }

    public AutorResponse salvarAutor(AutorRequest autorRequest) {

        if (autorRepository.existsByCpf(autorRequest.cpf())) {
            throw new IllegalArgumentException("CPF já cadastrado!");
        }

        if (!DataValidator.dataNascimentoValida(autorRequest.dataDeNascimento())){
            throw new IllegalArgumentException("Data de nascimento no formato inválido ou no futuro! Favor fornecer data no formato aaaa-mm-dd!");

        }

        if (autorRequest.sexo() != null){
            if(!DataValidator.sexoValido(autorRequest.sexo())){
                throw new IllegalArgumentException("Sexo inválido. Valores permitidos: Masculino, Feminino ou Outro!");
            }
        }

        Autor autor = new Autor(
                autorRequest.nome(),
                LocalDate.parse(autorRequest.dataDeNascimento()),
                autorRequest.cpf()
        );

        autor.setSexo(autorRequest.sexo());

        Autor autorSalvo = autorRepository.save(autor);

        return new AutorResponse(
                autor.getId(),
                autorSalvo.getNome(),
                autorSalvo.getSexo(),
                autorSalvo.getDataDeNascimento().toString(),
                autorSalvo.getCpf(),
                autor.getLivros()
                        .stream()
                        .map(Livro::getId)
                        .toList()
        );
    }

    public AutorResponse atualizarAutor(Long id, AutorUpdateRequest autorUpdate) {


        Autor autor = autorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Autor com Id " + id + " não encontrado!"));

        if (autorUpdate.nome() != null) {
            if(autorUpdate.nome().isBlank()){
                throw new IllegalArgumentException("Nome não pode ser vazio!");
            }
            autor.setNome(autorUpdate.nome());

        }

        if (autorUpdate.sexo() != null){
            if(!DataValidator.sexoValido(autorUpdate.sexo())){
                throw new IllegalArgumentException("Sexo inválido. Valores permitidos: Masculino, Feminino ou Outro!");
            }
            autor.setSexo(autorUpdate.sexo());
        }

        if (autorUpdate.dataDeNascimento() != null) {
            if (!DataValidator.dataNascimentoValida(autorUpdate.dataDeNascimento())){
                throw new IllegalArgumentException("Data de nascimento no formato invalido ou no futuro!");

            }
            autor.setDataDeNascimento(LocalDate.parse(autorUpdate.dataDeNascimento()));
        }

        if (autorUpdate.cpf() != null) {
            if (autorRepository.existsByCpf(autorUpdate.cpf())) {
                throw new IllegalArgumentException("CPF já cadastrado!");
            }
            autor.setCPF(autorUpdate.cpf());
        }

        Autor autorAtualizado = autorRepository.save(autor);

        return new AutorResponse(
                autor.getId(),
                autorAtualizado.getNome(),
                autorAtualizado.getSexo(),
                autorAtualizado.getDataDeNascimento().toString(),
                autorAtualizado.getCpf(),
                autor.getLivros()
                        .stream()
                        .map(Livro::getId)
                        .toList()
        );
    }

    public void deletarAutor(Long id) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Autor com Id " + id + " não encontrado!"));

        if (!autor.getLivros().isEmpty()) {
            throw new RuntimeException(
                    "Não é possível excluir um autor que tenha livros associados!"
            );
        }

        autorRepository.delete(autor);
    }
}