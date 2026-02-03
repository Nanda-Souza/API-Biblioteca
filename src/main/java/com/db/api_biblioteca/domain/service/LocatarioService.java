package com.db.api_biblioteca.domain.service;

import com.db.api_biblioteca.domain.dto.LocatarioResponse;
import com.db.api_biblioteca.domain.entity.Aluguel;
import com.db.api_biblioteca.domain.entity.Locatario;
import com.db.api_biblioteca.domain.repository.LocatarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocatarioService {

    private final LocatarioRepository locatarioRepository;

    public LocatarioService(LocatarioRepository locatarioRepository) { this.locatarioRepository = locatarioRepository; }

    public List<LocatarioResponse> listarLocatarios() {

        return locatarioRepository.findAll()
                .stream()
                .map( locatario -> new LocatarioResponse(
                        locatario.getId(),
                        locatario.getNome(),
                        locatario.getSexo(),
                        locatario.getTelefone(),
                        locatario.getEmail(),
                        locatario.getDataDeNascimento().toString(),
                        locatario.getCpf(),
                        locatario.getAlugueis()
                                .stream()
                                .map(Aluguel::getId)
                                .toList()
                ))
                .toList();
    }


}
