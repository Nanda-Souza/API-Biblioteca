package com.db.api_biblioteca.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "livro")

public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String isbn;
    private LocalDate dataDePublicacao;

    @ManyToMany
    @JoinTable(
            name = "autor_livro",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Autor> autores = new ArrayList<>();

    protected Livro() {
    }

    public Livro(String nome, String isbn, LocalDate dataDePublicacao) {
        this.nome = nome;
        this.isbn = isbn;
        this.dataDePublicacao = dataDePublicacao;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbd(String isbd) {
        this.isbn = isbn;
    }

    public LocalDate getDataDePublicacao() {
        return dataDePublicacao;
    }

    public void setDataDePublicacao(LocalDate dataDePublicacao) {
        this.dataDePublicacao = dataDePublicacao;
    }


}
