package com.db.api_biblioteca.domain.service;

import com.db.api_biblioteca.domain.dto.LivroRequest;
import com.db.api_biblioteca.domain.dto.LivroResponse;
import com.db.api_biblioteca.domain.entity.Autor;
import com.db.api_biblioteca.domain.entity.Livro;
import com.db.api_biblioteca.domain.repository.AutorRepository;
import com.db.api_biblioteca.domain.repository.LivroRepository;
import com.db.api_biblioteca.domain.validation.DataValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;

    public LivroService(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;

    }

    public List<LivroResponse> listarLivros() {

        return livroRepository.findAll()
                .stream()
                .map(livro -> new LivroResponse(
                        livro.getId(),
                        livro.getNome(),
                        livro.getIsbd(),
                        livro.getDataDePublicacao().toString(),
                        livro.getAutores()
                                .stream()
                                .map(Autor::getId)
                                .toList()
                ))
                .toList();
    }

    public LivroResponse salvarLivro(LivroRequest livroRequest) {

        if (livroRepository.existsByIsbd(livroRequest.isbd())) {
            throw new IllegalArgumentException("ISBD já cadastrado!");
        }

        if (!DataValidator.dataDePublicacaoValida(livroRequest.dataDePublicacao())){
            throw new IllegalArgumentException("Data de publicação no formato inválido! Favor fornecer data no formato aaaa-mm-dd!");

        }

        List<Autor> autores = autorRepository.findAllById(livroRequest.autoresIds());

        if (autores.size() != livroRequest.autoresIds().size()) {
            throw new IllegalArgumentException("Um ou mais autores não encontrados!");
        }

        Livro livro = new Livro(
                livroRequest.nome(),
                livroRequest.isbd(),
                LocalDate.parse(livroRequest.dataDePublicacao())
        );

        livro.getAutores().addAll(autores);

        autores.forEach(autor -> autor.getLivros().add(livro));

        Livro livroSalvo = livroRepository.save(livro);

        return new LivroResponse(
                livroSalvo.getId(),
                livroSalvo.getNome(),
                livroSalvo.getIsbd(),
                livroSalvo.getDataDePublicacao().toString(),
                livroSalvo.getAutores()
                        .stream()
                        .map(Autor::getId)
                        .toList()
        );
    }

}
