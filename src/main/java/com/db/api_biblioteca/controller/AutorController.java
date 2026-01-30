package com.db.api_biblioteca.controller;

import com.db.api_biblioteca.domain.dto.AutorRequest;
import com.db.api_biblioteca.domain.dto.AutorResponse;
import com.db.api_biblioteca.domain.dto.AutorUpdateRequest;
import com.db.api_biblioteca.domain.entity.Autor;
import com.db.api_biblioteca.domain.repository.AutorRepository;
import com.db.api_biblioteca.domain.service.AutorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/autor")
public class AutorController {
    @Autowired
    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @GetMapping
    public ResponseEntity<List<AutorResponse>> listarAutores() {
        return ResponseEntity.ok(autorService.listarAutores());
    }

    @GetMapping("/buscar")
    public ResponseEntity<AutorResponse> buscarAutorPorNome(
            @RequestParam String nome
    ) {
        return ResponseEntity.ok(autorService.buscarAutorPorNomeCompleto(nome));
    }

    @PostMapping
    public ResponseEntity<AutorResponse> salvarAutor(
            @RequestBody @Valid AutorRequest autorRequest
    ) {
        return ResponseEntity.ok(autorService.salvarAutor(autorRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAutor(@PathVariable Long id) {
        autorService.deletarAutor(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutorResponse> atualizarAutor(
            @PathVariable Long id,
            @RequestBody AutorUpdateRequest autorRequest
    ) {
        return ResponseEntity.ok(autorService.atualizarAutor(id, autorRequest));
    }

}
