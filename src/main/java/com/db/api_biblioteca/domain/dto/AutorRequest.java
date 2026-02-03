package com.db.api_biblioteca.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AutorRequest (

        @NotBlank(message = "Nome é obrigatório!")
        String nome,

        String sexo,

        @NotNull(message = "Data de nascimento é obrigatória!")
        String dataDeNascimento,

        @NotBlank(message = "CPF é obrigatório!")
        @Pattern(
                regexp = "\\d{11}",
                message = "CPF deve conter 11 dígitos numéricos"
        )
        String cpf) {
}
