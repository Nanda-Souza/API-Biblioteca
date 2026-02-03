package com.db.api_biblioteca.domain.dto;

import java.util.List;

public record LivroResponse (
        Long id,
        String nome,
        String isbd,
        String dataDePublicacao,
        List<Long> autoresIds){
}
