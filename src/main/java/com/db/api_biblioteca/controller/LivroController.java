package com.db.api_biblioteca.controller;

import com.db.api_biblioteca.domain.dto.LivroRequest;
import com.db.api_biblioteca.domain.dto.LivroResponse;
import com.db.api_biblioteca.domain.dto.LivroPorAutorResponse;
import com.db.api_biblioteca.domain.dto.LivroUpdateRequest;
import com.db.api_biblioteca.domain.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livro")
public class LivroController {
    @Autowired
    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping
    public ResponseEntity<List<LivroResponse>> listarLivros() {

        return ResponseEntity.ok(livroService.listarLivros());
    }

    @GetMapping("/{livroId}")
    public ResponseEntity<LivroResponse> buscarLivroPorId(
            @PathVariable Long livroId
    ) {
        return ResponseEntity.ok(livroService.buscarLivroPorId(livroId));
    }

    @GetMapping("/autor/{autorId}/listar")
    public ResponseEntity<List<LivroPorAutorResponse>> listarLivrosPorIdAutor(
            @PathVariable Long autorId
    ) {
        return ResponseEntity.ok(livroService.listarLivrosPorIdAutor(autorId));
    }

    @PostMapping
    public ResponseEntity<LivroResponse> salvarLivro(
            @RequestBody @Valid LivroRequest livroRequest
    ) {
        return ResponseEntity.ok(livroService.salvarLivro(livroRequest));
    }

    @PutMapping("adicionar/{livroId}/autor/{autorId}")
    public ResponseEntity<LivroResponse> adicionarAutorAoLivro(
            @PathVariable Long livroId,
            @PathVariable Long autorId
    ) {
        LivroResponse response =
                livroService.adicionarAutorAoLivro(livroId, autorId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("remover/{livroId}/autor/{autorId}")
    public ResponseEntity<LivroResponse> removerAutorDoLivro(
            @PathVariable Long livroId,
            @PathVariable Long autorId
    ) {
        LivroResponse response =
                livroService.removerAutorDoLivro(livroId, autorId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{livroId}")
    public ResponseEntity<Void> deletarLivroPorId(@PathVariable Long livroId) {
        livroService.deletarLivro(livroId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{livroId}")
    public ResponseEntity<LivroResponse> atualizarLivro(
            @PathVariable Long livroId,
            @RequestBody @Valid LivroUpdateRequest livroRequest
    ){
        return ResponseEntity.ok(livroService.atualizarLivro(livroId, livroRequest));
    }

}
