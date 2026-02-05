package com.db.api_biblioteca.domain.service;

import com.db.api_biblioteca.domain.dto.*;
import com.db.api_biblioteca.domain.entity.Aluguel;
import com.db.api_biblioteca.domain.entity.Autor;
import com.db.api_biblioteca.domain.entity.Livro;
import com.db.api_biblioteca.domain.entity.Locatario;
import com.db.api_biblioteca.domain.repository.LocatarioRepository;
import com.db.api_biblioteca.domain.validation.DataValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LocatarioService {

    private final LocatarioRepository locatarioRepository;

    public LocatarioService(LocatarioRepository locatarioRepository) { this.locatarioRepository = locatarioRepository; }

    public List<LocatarioResponse> listarLocatarios() {

        return locatarioRepository.findAll()
                .stream()
                .map( locatario -> new LocatarioResponse(
                        locatario.getId(),
                        locatario.getNome(),
                        locatario.getSexo(),
                        locatario.getTelefone(),
                        locatario.getEmail(),
                        locatario.getDataDeNascimento().toString(),
                        locatario.getCpf(),
                        locatario.getAlugueis()
                                .stream()
                                .map(Aluguel::getId)
                                .toList()
                ))
                .toList();
    }

    public LocatarioResponse salvarLocatario(LocatarioRequest locatarioRequest) {

        if (locatarioRepository.existsByCpf(locatarioRequest.cpf())) {
            throw new IllegalArgumentException("CPF já cadastrado!");
        }

        if (locatarioRepository.existsByEmail(locatarioRequest.email())) {
            throw new IllegalArgumentException("Email já cadastrado!");
        }

        if (!DataValidator.dataNascimentoValida(locatarioRequest.dataDeNascimento())){
            throw new IllegalArgumentException("Data de nascimento no formato inválido ou no futuro! Favor fornecer data no formato aaaa-mm-dd!");

        }

        if (locatarioRequest.sexo() != null){
            if(!DataValidator.sexoValido(locatarioRequest.sexo())){
                throw new IllegalArgumentException("Sexo inválido. Valores permitidos: Masculino, Feminino ou Outro!");
            }
        }

        Locatario locatario = new Locatario(
                locatarioRequest.nome(),
                locatarioRequest.telefone(),
                locatarioRequest.email(),
                LocalDate.parse(locatarioRequest.dataDeNascimento()),
                locatarioRequest.cpf()
        );

        locatario.setSexo(locatarioRequest.sexo());

        Locatario locatarioSalvo = locatarioRepository.save(locatario);

        return new LocatarioResponse(
                locatario.getId(),
                locatarioSalvo.getNome(),
                locatarioSalvo.getSexo(),
                locatarioSalvo.getTelefone(),
                locatarioSalvo.getEmail(),
                locatarioSalvo.getDataDeNascimento().toString(),
                locatarioSalvo.getCpf(),
                locatario.getAlugueis()
                        .stream()
                        .map(Aluguel::getId)
                        .toList()
        );

    }

    public LocatarioResponse atualizarLocatario(Long id, LocatarioUpdateRequest locatarioUpdate) {


        Locatario locatario = locatarioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Locatario com Id " + id + " não encontrado!"));

        if (locatarioUpdate.nome() != null) {
            if(locatarioUpdate.nome().isBlank()){
                throw new IllegalArgumentException("Nome não pode ser vazio!");
            }
            locatario.setNome(locatarioUpdate.nome());

        }

        if (locatarioUpdate.sexo() != null){
            if(!DataValidator.sexoValido(locatarioUpdate.sexo())){
                throw new IllegalArgumentException("Sexo inválido. Valores permitidos: Masculino, Feminino ou Outro!");
            }
            locatario.setSexo(locatarioUpdate.sexo());
        }

        if (locatarioUpdate.telefone() != null) {
            if(locatarioUpdate.telefone().isBlank()){
                throw new IllegalArgumentException("Telefone não pode ser vazio!");
            }
            locatario.setTelefone(locatarioUpdate.telefone());

        }

        if (locatarioUpdate.email() != null) {
            if (locatarioRepository.existsByEmail(locatarioUpdate.email())) {
                throw new IllegalArgumentException("Email já cadastrado!");
            }
            locatario.setEmail(locatarioUpdate.email());
        }

        if (locatarioUpdate.dataDeNascimento() != null) {
            if (!DataValidator.dataNascimentoValida(locatarioUpdate.dataDeNascimento())){
                throw new IllegalArgumentException("Data de nascimento no formato inválido ou no futuro!");

            }
            locatario.setDataDeNascimento(LocalDate.parse(locatarioUpdate.dataDeNascimento()));
        }

        if (locatarioUpdate.cpf() != null) {
            if (locatarioRepository.existsByCpf(locatarioUpdate.cpf())) {
                throw new IllegalArgumentException("CPF já cadastrado!");
            }
            locatario.setCpf(locatarioUpdate.cpf());
        }

        Locatario locatarioAtualizado = locatarioRepository.save(locatario);

        return new LocatarioResponse(
                locatario.getId(),
                locatarioAtualizado.getNome(),
                locatarioAtualizado.getSexo(),
                locatarioAtualizado.getTelefone(),
                locatarioAtualizado.getEmail(),
                locatarioAtualizado.getDataDeNascimento().toString(),
                locatarioAtualizado.getCpf(),
                locatario.getAlugueis()
                        .stream()
                        .map(Aluguel::getId)
                        .toList()
        );
    }


}
