package com.db.api_biblioteca.domain.dto;

public record LivroPorAutorResponse(
        Long id,
        String nome,
        String isbd,
        String dataDePublicacao){
}
