package com.db.api_biblioteca.domain.dto;

import java.util.List;

public record AutorRequest (String nome, String sexo, String dataDeNascimento, String cpf, List<Long> livrosIds) {
}
