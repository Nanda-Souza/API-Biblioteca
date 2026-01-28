package com.db.api_biblioteca.domain.service;

import com.db.api_biblioteca.domain.dto.AutorRequest;
import com.db.api_biblioteca.domain.dto.AutorResponse;
import com.db.api_biblioteca.domain.entity.Autor;
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
                        autor.getNome(),
                        autor.getSexo(),
                        autor.getDataDeNascimento().toString(),
                        autor.getCpf(),
                        List.of() // sem livros por enquanto
                ))
                .toList();
    }

    public AutorResponse salvarAutor(AutorRequest autorRequest) {

        if (autorRepository.existsByCpf(autorRequest.cpf())) {
            throw new IllegalArgumentException("CPF já cadastrado!");
        }

        if (!DataValidator.dataNascimentoValida(autorRequest.dataDeNascimento())){
            throw new IllegalArgumentException("Data de nascimento no formato invalido ou no futuro!");

        }

        Autor autor = new Autor(
                autorRequest.nome(),
                LocalDate.parse(autorRequest.dataDeNascimento()),
                autorRequest.cpf()
        );

        //autor.setSexo(autorRequest.sexo());

        Autor autorSalvo = autorRepository.save(autor);

        return new AutorResponse(
                autorSalvo.getNome(),
                autorSalvo.getSexo(),
                autorSalvo.getDataDeNascimento().toString(),
                autorSalvo.getCpf(),
                List.of() // sem livros por enquanto
        );
    }
}