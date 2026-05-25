package br.ufrn.eaj.lojasuplementos.service;

import br.ufrn.eaj.lojasuplementos.domain.Suplementos;
import br.ufrn.eaj.lojasuplementos.exception.RecursoNaoEncontradoException;
import br.ufrn.eaj.lojasuplementos.repository.SuplementosRepository;
import org.springframework.stereotype.Service;

@Service
public class SuplementosService {

    private final SuplementosRepository repository;

    public SuplementosService(SuplementosRepository repository) {
        this.repository = repository;
    }

    public Suplementos buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Suplemento não encontrado com o ID: " + id));
    }
}