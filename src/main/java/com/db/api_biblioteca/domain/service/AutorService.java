package com.db.api_biblioteca.domain.service;

import com.db.api_biblioteca.domain.dto.AutorRequest;
import com.db.api_biblioteca.domain.dto.AutorResponse;
import com.db.api_biblioteca.domain.entity.Autor;
import com.db.api_biblioteca.domain.repository.AutorRepository;
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
                        autor.getCPF(),
                        List.of() // sem livros por enquanto
                ))
                .toList();
    }

    public AutorResponse salvarAutor(AutorRequest autorRequest) {

        Autor autor = new Autor(
                autorRequest.nome(),
                LocalDate.parse(autorRequest.dataDeNascimento()),
                autorRequest.cpf()
        );

        // campo opcional
        autor.setSexo(autorRequest.sexo());

        Autor autorSalvo = autorRepository.save(autor);

        return new AutorResponse(
                autorSalvo.getNome(),
                autorSalvo.getSexo(),
                autorSalvo.getDataDeNascimento().toString(),
                autorSalvo.getCPF(),
                List.of() // sem livros por enquanto
        );
    }
}