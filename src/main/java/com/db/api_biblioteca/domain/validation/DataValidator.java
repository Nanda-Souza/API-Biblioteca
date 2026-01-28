package com.db.api_biblioteca.domain.validation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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
}