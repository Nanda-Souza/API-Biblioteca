package com.db.api_biblioteca.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record LivroRequest (

        @NotBlank(message = "Nome é obrigatório!")
        String nome,

        @NotBlank(message = "ISBD é obrigatório!")
        @Pattern(
                regexp = "^(97[89]\\d{10})$",
                message = "ISBN deve conter 13 dígitos numéricos começando por 978 ou 979!"
        )
        String isbd,

        @NotNull(message = "Data de nascimento é obrigatória!")
        String dataDeNascimento) {
}
