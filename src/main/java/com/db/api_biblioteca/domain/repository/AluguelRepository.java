package com.db.api_biblioteca.domain.repository;

import com.db.api_biblioteca.domain.entity.Aluguel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AluguelRepository extends JpaRepository<Aluguel, Long> {
    boolean existsByLivros_Id(Long livroId);
    boolean existsByLocatario_Id(Long locatarioId);

    @Query("""
    SELECT DISTINCT l.id
    FROM Aluguel a
    JOIN a.livros l
""")
    List<Long> buscarIdsLivrosAlugados();
}
