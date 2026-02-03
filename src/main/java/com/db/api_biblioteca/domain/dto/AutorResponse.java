package com.db.api_biblioteca.domain.dto;

import com.db.api_biblioteca.domain.entity.Livro;

import java.util.List;

public record AutorResponse (
        Long id,
        String nome,
        String sexo,
        String dataDeNascimento,
        String cpf,
        List<Long> livrosIds){
}
