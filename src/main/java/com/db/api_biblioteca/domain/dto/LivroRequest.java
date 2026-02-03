package com.db.api_biblioteca.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record LivroRequest (

        @NotBlank(message = "Nome é obrigatório!")
        String nome,

        @NotBlank(message = "ISBD é obrigatório!")
        @Pattern(
                regexp = "^(97[89]\\d{10})$",
                message = "ISBN deve conter 13 dígitos numéricos começando por 978 ou 979!"
        )
        String isbd,

        @NotNull(message = "Data de publicação é obrigatória!")
        String dataDePublicacao,

        @NotNull(message = "O livro deve possuir ao menos um autor!")
        @Size(min = 1, message = "O livro deve possuir ao menos um autor!")
        List<Long> autoresIds) {
}
