package com.db.api_biblioteca.domain.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record LivroUpdateRequest(
    String nome,
    @Pattern(
            regexp = "^(97[89]\\d{10})$",
            message = "ISBN deve conter 13 dígitos numéricos começando por 978 ou 979!"
    )
    String isbn,
    String dataDePublicacao,

    @Size(min = 1, message = "O livro deve possuir ao menos um autor!")
    List<Long> autoresIds) {

}
