package com.db.api_biblioteca.domain.dto;

import jakarta.validation.constraints.Pattern;

public record AutorUpdateRequest(

        String nome,
        String sexo,
        String dataDeNascimento,
        @Pattern(
                regexp = "\\d{11}",
                message = "CPF deve conter 11 dígitos numéricos"
        )
        String cpf) {
}
