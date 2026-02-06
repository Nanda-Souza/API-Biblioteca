package com.db.api_biblioteca.domain.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record AluguelRequest(

        @NotNull(message = "O locatário é obrigatório!")
        Long locatarioId,

        @NotNull(message = "O aluguel deve possuir ao menos um livro!")
        @Size(min = 1, message = "O aluguel deve possuir ao menos um livro!")
        List<Long> livrosIds) {
}
