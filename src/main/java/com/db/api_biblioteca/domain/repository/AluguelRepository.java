package com.db.api_biblioteca.domain.repository;

import com.db.api_biblioteca.domain.entity.Aluguel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AluguelRepository extends JpaRepository<Aluguel, Long> {
}
