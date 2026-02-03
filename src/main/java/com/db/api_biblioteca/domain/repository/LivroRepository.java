package com.db.api_biblioteca.domain.repository;

import com.db.api_biblioteca.domain.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    boolean existsByIsbn(String isbn);
}
