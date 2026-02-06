package com.db.api_biblioteca.controller;

import com.db.api_biblioteca.domain.dto.AluguelRequest;
import com.db.api_biblioteca.domain.dto.AluguelResponse;
import com.db.api_biblioteca.domain.dto.LivroResponse;
import com.db.api_biblioteca.domain.service.AluguelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aluguel")
public class AluguelController {
    @Autowired
    private final AluguelService aluguelService;

    public AluguelController(AluguelService aluguelService) {this.aluguelService = aluguelService;}

    @GetMapping
    public ResponseEntity<List<AluguelResponse>> listarAlugueis() {

        return ResponseEntity.ok(aluguelService.listarAlugueis());
    }

    @PostMapping
    public ResponseEntity<AluguelResponse> salvarAluguel(
            @RequestBody @Valid AluguelRequest aluguelRequest) {
        return ResponseEntity.ok(aluguelService.salvarAluguel(aluguelRequest));
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<LivroResponse>> listarLivrosDisponiveis() {

        return ResponseEntity.ok(aluguelService.listarLivrosDisponiveis());
    }

    @GetMapping("/alugados")
    public ResponseEntity<List<LivroResponse>> listarLivrosAlugados() {

        return ResponseEntity.ok(aluguelService.listarLivrosAlugados());
    }

    @GetMapping("/locatario/{id}")
    public ResponseEntity<List<LivroResponse>> listarLivrosAlugadosPorLocatario(@PathVariable Long id) {
        return ResponseEntity.ok(aluguelService.listarLivrosAlugadosPorLocatario(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirAluguel(@PathVariable Long id) {
        aluguelService.deletarAluguel(id);
        return ResponseEntity.noContent().build();
    }


}
