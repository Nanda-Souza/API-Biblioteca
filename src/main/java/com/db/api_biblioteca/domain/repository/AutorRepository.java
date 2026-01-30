package com.db.api_biblioteca.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.db.api_biblioteca.domain.entity.Autor;

import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    boolean existsByCpf(String cpf);
    Optional<Autor> findByNomeIgnoreCase(String nome);
    Optional<Autor> findById(Long id);
}
