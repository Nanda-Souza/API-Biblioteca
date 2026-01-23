package com.db.api_biblioteca.domain.entity;

import java.time.LocalDate;
import java.util.ArrayList;

public class Aluguel {
    private LocalDate dataRetirada;
    private LocalDate dataDevolucao;
    private ArrayList<Livro> livros;
    private ArrayList<Locatario> locatarios;

    public Aluguel(LocalDate dataRetirada, LocalDate dataDevolucao, ArrayList<Livro> livros, ArrayList<Locatario> locatarios) {
        this.dataRetirada = dataRetirada;
        this.dataDevolucao = dataDevolucao;
        this.livros = livros;
        this.locatarios = locatarios;
    }

    public LocalDate getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(LocalDate dataRetirada) {
        this.dataRetirada = dataRetirada;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public ArrayList<Livro> getLivros() {
        return livros;
    }

    public void setLivros(ArrayList<Livro> livros) {
        this.livros = livros;
    }

    public ArrayList<Locatario> getLocatarios() {
        return locatarios;
    }

    public void setLocatarios(ArrayList<Locatario> locatarios) {
        this.locatarios = locatarios;
    }
}
