package com.db.api_biblioteca.domain.dto;

import jakarta.validation.constraints.Pattern;

public record LocatarioUpdateRequest(
        String nome,
        String sexo,

        @Pattern(
                regexp = "\\d{9}",
                message = "Telefone deve conter 9 dígitos numericos e sem DDD!"
        )
        String telefone,

        @Pattern(
                regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}",
                message = "Email deve ser no formato valido email@.com.br!"
        )
        String email,


        String dataDeNascimento,
        @Pattern(
                regexp = "\\d{11}",
                message = "CPF deve conter 11 dígitos numéricos"
        )
        String cpf) {
}