package com.db.api_biblioteca.domain.dto;

import com.db.api_biblioteca.domain.entity.Autor;

import java.util.List;

public record LivroResponse (
        Long id,
        String nome,
        String isbd,
        String dataDePublicacao,
        List<Autor> autores){
}
