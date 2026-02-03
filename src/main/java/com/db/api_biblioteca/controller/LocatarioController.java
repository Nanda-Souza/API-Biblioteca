package com.db.api_biblioteca.controller;

import com.db.api_biblioteca.domain.dto.LocatarioRequest;
import com.db.api_biblioteca.domain.dto.LocatarioResponse;
import com.db.api_biblioteca.domain.entity.Locatario;
import com.db.api_biblioteca.domain.service.LocatarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locatario")
public class LocatarioController {
    @Autowired
    private final LocatarioService locatarioService;

    public LocatarioController(LocatarioService locatarioService) {
        this.locatarioService = locatarioService;
    }

    @GetMapping
    public ResponseEntity<List<LocatarioResponse>> listarLocatarios() {
        return ResponseEntity.ok(locatarioService.listarLocatarios());
    }

    @PostMapping
    public ResponseEntity<LocatarioResponse> salvarLocatario(
            @RequestBody @Valid LocatarioRequest locatarioRequest
    ) {
       return ResponseEntity.ok(locatarioService.salvarLocatario(locatarioRequest));
    }
}
