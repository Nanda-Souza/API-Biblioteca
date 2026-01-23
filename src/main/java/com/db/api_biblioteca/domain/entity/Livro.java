package com.db.api_biblioteca.domain.entity;

import java.time.LocalDate;
import java.util.ArrayList;

public class Livro {
    private String nome;
    private String ISBD;
    private LocalDate dataDePublicacao;
    private ArrayList<Autor> autores;

    public Livro(String nome, String ISBD, LocalDate dataDePublicacao,  ArrayList<Autor> autores) {
        this.nome = nome;
        this.ISBD = ISBD;
        this.dataDePublicacao = dataDePublicacao;
        this.autores = autores;
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

    public ArrayList<Autor> getAutores() {
        return autores;
    }

    public void setAutores(ArrayList<Autor> autores) {
        this.autores = autores;
    }
}
