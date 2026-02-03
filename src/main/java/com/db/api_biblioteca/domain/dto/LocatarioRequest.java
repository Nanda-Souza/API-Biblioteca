package com.db.api_biblioteca.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record LocatarioRequest (

        @NotBlank(message = "Nome é obrigatório!")
        String nome,

        String sexo,

        @NotBlank(message = "Telefone é obrigatório!")
        @Pattern(
                regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}",
                message = "Email deve ser no formato valido email@.com.br!"
        )
        String telefone,

        @NotBlank(message = "Email é obrigatório!")
        String email,

        @NotNull(message = "Data de nascimento é obrigatória!")
        String dataDeNascimento,

        @NotBlank(message = "CPF é obrigatório!")
        @Pattern(
                regexp = "\\d{11}",
                message = "CPF deve conter 11 dígitos numéricos"
        )
        String cpf) {
}
