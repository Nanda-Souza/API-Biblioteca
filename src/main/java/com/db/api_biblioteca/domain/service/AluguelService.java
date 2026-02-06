package com.db.api_biblioteca.domain.service;

import com.db.api_biblioteca.domain.dto.AluguelResponse;
import com.db.api_biblioteca.domain.dto.LivroResponse;
import com.db.api_biblioteca.domain.entity.Autor;
import com.db.api_biblioteca.domain.entity.Livro;
import com.db.api_biblioteca.domain.repository.AluguelRepository;
import com.db.api_biblioteca.domain.repository.LivroRepository;
import com.db.api_biblioteca.domain.repository.LocatarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AluguelService {

    private final AluguelRepository aluguelRepository;
    private final LivroRepository livroRepository;
    private final LocatarioRepository locatarioRepository;

    public AluguelService(AluguelRepository aluguelRepository, LivroRepository livroRepository, LocatarioRepository locatarioRepository) {
        this.aluguelRepository = aluguelRepository;
        this.livroRepository = livroRepository;
        this.locatarioRepository = locatarioRepository;
    }

    public List<AluguelResponse> listarAlugueis() {

        return aluguelRepository.findAll()
                .stream()
                .map(aluguel -> new AluguelResponse(
                        aluguel.getId(),
                        aluguel.getDataRetirada().toString(),
                        aluguel.getDataDevolucao().toString(),
                        aluguel.getLocatario().getId(),
                        aluguel.getLivros()
                                .stream()
                                .map(Livro::getId)
                                .toList()
                ))
                .toList();
    }




}
