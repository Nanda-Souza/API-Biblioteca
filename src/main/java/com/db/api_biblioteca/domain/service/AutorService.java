package com.db.api_biblioteca.domain.service;

import com.db.api_biblioteca.domain.dto.AutorResponse;
import com.db.api_biblioteca.domain.repository.AutorRepository;
import org.springframework.stereotype.Service;

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
}