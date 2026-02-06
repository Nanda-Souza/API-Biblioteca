package com.db.api_biblioteca.domain.dto;

import jakarta.validation.constraints.Size;

import java.util.List;

public record AluguelUpdateRequest(

        Long locatarioId,
        List<Long> livrosIds) {
}
