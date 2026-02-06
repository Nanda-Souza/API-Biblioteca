package com.db.api_biblioteca.domain.dto;

import java.util.List;

public record AluguelResponse(
        Long id,
        String dataRetirada,
        String dataDevolucao,
        Long locatarioId,
        List<Long> livrosIds

) {
}
