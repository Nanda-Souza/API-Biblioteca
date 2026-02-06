package com.db.api_biblioteca.domain.service;

import com.db.api_biblioteca.domain.dto.AluguelRequest;
import com.db.api_biblioteca.domain.dto.AluguelResponse;
import com.db.api_biblioteca.domain.dto.LivroResponse;
import com.db.api_biblioteca.domain.entity.Aluguel;
import com.db.api_biblioteca.domain.entity.Autor;
import com.db.api_biblioteca.domain.entity.Livro;
import com.db.api_biblioteca.domain.entity.Locatario;
import com.db.api_biblioteca.domain.repository.AluguelRepository;
import com.db.api_biblioteca.domain.repository.LivroRepository;
import com.db.api_biblioteca.domain.repository.LocatarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public AluguelResponse salvarAluguel(AluguelRequest aluguelRequest) {

        Locatario locatario = locatarioRepository.findById(aluguelRequest.locatarioId())
                .orElseThrow(() ->
                        new RuntimeException("Locatário com id " + aluguelRequest.locatarioId() + " não encontrado!")
                );

        List<Livro> livros = livroRepository.findAllById(aluguelRequest.livrosIds());

        if (livros.size() != aluguelRequest.livrosIds().size()) {
            throw new RuntimeException("Um ou mais livros não foram encontrados!");
        }

        Aluguel aluguel = new Aluguel(locatario, livros);

        Aluguel aluguelSalvo = aluguelRepository.save(aluguel);

        return new AluguelResponse(
                aluguelSalvo.getId(),
                aluguelSalvo.getDataRetirada().toString(),
                aluguelSalvo.getDataDevolucao().toString(),
                aluguelSalvo.getLocatario().getId(),
                aluguelSalvo.getLivros()
                        .stream()
                        .map(Livro::getId)
                        .toList()
        );

    }

    public List<LivroResponse> listarLivrosDisponiveis() {

        List<Long> livrosAlugadosIds = aluguelRepository.buscarIdsLivrosAlugados();

        return livroRepository.findAll()
                .stream()
                .filter(livro -> !livrosAlugadosIds.contains(livro.getId()))
                .map(livro -> new LivroResponse(
                        livro.getId(),
                        livro.getNome(),
                        livro.getIsbn(),
                        livro.getDataDePublicacao().toString(),
                        livro.getAutores()
                                .stream()
                                .map(Autor::getId)
                                .toList()
                ))
                .toList();
    }

    public List<LivroResponse> listarLivrosAlugados() {

        return aluguelRepository.findAll()
                .stream()
                .flatMap(aluguel -> aluguel.getLivros().stream())
                .distinct()
                .map(livro -> new LivroResponse(
                        livro.getId(),
                        livro.getNome(),
                        livro.getIsbn(),
                        livro.getDataDePublicacao().toString(),
                        livro.getAutores()
                                .stream()
                                .map(Autor::getId)
                                .toList()
                ))
                .toList();
    }

    public List<LivroResponse> listarLivrosAlugadosPorLocatario(Long locatarioId) {

        Locatario locatario = locatarioRepository.findById(locatarioId)
                .orElseThrow(() ->
                        new RuntimeException("Locatário com id " + locatarioId + " não encontrado!")
                );

        return locatario.getAlugueis()
                .stream()
                .flatMap(aluguel -> aluguel.getLivros().stream())
                .distinct()
                .map(livro -> new LivroResponse(
                        livro.getId(),
                        livro.getNome(),
                        livro.getIsbn(),
                        livro.getDataDePublicacao().toString(),
                        livro.getAutores()
                                .stream()
                                .map(Autor::getId)
                                .toList()
                ))
                .toList();
    }

    public void deletarAluguel(Long aluguelId) {

        Aluguel aluguel = aluguelRepository.findById(aluguelId)
                .orElseThrow(() ->
                        new RuntimeException("Aluguel com id " + aluguelId + " não encontrado!")
                );

        aluguelRepository.delete(aluguel);
    }







}
