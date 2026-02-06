package com.db.api_biblioteca.domain.dto;

import java.util.List;

public record LocatarioResponse(
        Long id,
        String nome,
        String sexo,
        String telefone,
        String email,
        String dataDeNascimento,
        String cpf,
        List<Long> alugueisIds) {
}
