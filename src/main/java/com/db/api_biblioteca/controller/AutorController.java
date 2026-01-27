package com.db.api_biblioteca.controller;

import com.db.api_biblioteca.domain.entity.Autor;
import com.db.api_biblioteca.domain.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/autor")
public class AutorController {
    @Autowired
    private AutorRepository autorRepository;

    @GetMapping
    public ResponseEntity getAutores() {
        System.out.println(autorRepository.findAll());
        return ResponseEntity.ok(autorRepository.findAll());
    }

    @PostMapping
    public ResponseEntity saveAutor(@RequestBody Autor autor) {
        Autor saveAutor = autorRepository.save(autor);
        return ResponseEntity.ok(saveAutor);
    }
}
