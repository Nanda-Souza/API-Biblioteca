package com.db.api_biblioteca.domain.entity;

import java.time.LocalDate;

public class Livro {
    private String nome;
    private String ISBD;
    private LocalDate dataDePublicacao;

    public Livro(String nome, String ISBD, LocalDate dataDePublicacao) {
        this.nome = nome;
        this.ISBD = ISBD;
        this.dataDePublicacao = dataDePublicacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getISBD() {
        return ISBD;
    }

    public void setISBD(String ISBD) {
        this.ISBD = ISBD;
    }

    public LocalDate getDataDePublicacao() {
        return dataDePublicacao;
    }

    public void setDataDePublicacao(LocalDate dataDePublicacao) {
        this.dataDePublicacao = dataDePublicacao;
    }

}
