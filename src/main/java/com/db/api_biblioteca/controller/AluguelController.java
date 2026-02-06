package com.db.api_biblioteca.controller;

import com.db.api_biblioteca.domain.dto.AluguelResponse;
import com.db.api_biblioteca.domain.service.AluguelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
