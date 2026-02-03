package com.db.api_biblioteca.domain.repository;

import com.db.api_biblioteca.domain.entity.Locatario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocatarioRepository extends JpaRepository<Locatario, Long> {
}
