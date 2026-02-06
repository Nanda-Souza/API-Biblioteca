package com.db.api_biblioteca.domain.repository;

import com.db.api_biblioteca.domain.entity.Locatario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocatarioRepository extends JpaRepository<Locatario, Long> {
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
    Optional<Locatario> findById(Long id);
}
