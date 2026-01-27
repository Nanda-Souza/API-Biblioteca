package com.db.api_biblioteca.controller;

import com.db.api_biblioteca.domain.dto.AutorResponse;
import com.db.api_biblioteca.domain.entity.Autor;
import com.db.api_biblioteca.domain.repository.AutorRepository;
import com.db.api_biblioteca.domain.service.AutorService;
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
    public ResponseEntity<List<AutorResponse>> getAutores() {
        return ResponseEntity.ok(autorService.listarAutores());
    }

    /*@PostMapping
    public ResponseEntity saveAutor(@RequestBody Autor autor) {
        Autor saveAutor = autorRepository.save(autor);
        return ResponseEntity.ok(saveAutor);
    }*/
}
