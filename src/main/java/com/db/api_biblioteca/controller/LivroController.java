package com.db.api_biblioteca.controller;

import com.db.api_biblioteca.domain.dto.LivroRequest;
import com.db.api_biblioteca.domain.dto.LivroResponse;
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

    @PostMapping
    public ResponseEntity<LivroResponse> salvarLivro(
            @RequestBody @Valid LivroRequest livroRequest
    ) {
        return ResponseEntity.ok(livroService.salvarLivro(livroRequest));
    }

}
