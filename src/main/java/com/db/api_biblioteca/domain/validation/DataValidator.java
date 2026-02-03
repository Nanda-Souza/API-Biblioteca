package com.db.api_biblioteca.domain.validation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class DataValidator {

    private DataValidator() {
    }

    public static boolean dataNascimentoValida(String data) {
        try {
            LocalDate nascimento = LocalDate.parse(data);
            return !nascimento.isAfter(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean dataDePublicacaoValida(String data) {
        try {
            LocalDate dataDePublicacao = LocalDate.parse(data);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean sexoValido(String data) {
        List<String> sexosValidos = new ArrayList<>();
        sexosValidos.add("Masculino");
        sexosValidos.add("Feminino");
        sexosValidos.add("Outro");

        if(sexosValidos.contains(data)) {
            return true;
        }

        return false;
    }
}