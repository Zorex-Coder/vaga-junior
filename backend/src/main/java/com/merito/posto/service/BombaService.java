package com.merito.posto.service;

import com.merito.posto.model.Bomba;
import com.merito.posto.repository.BombaRepository;

import java.util.List;

/**
 * Classe responsável pelas regras de negócio da entidade Bomba.
 */
public class BombaService {

    private final BombaRepository repository;

    public BombaService(BombaRepository repository) {
        this.repository = repository;
    }

    public Bomba criar(Bomba bomba) {
        validarDadosObrigatorios(bomba);
        return repository.salvar(bomba);
    }

    public List<Bomba> listar() {
        return repository.listarTodos();
    }

    public Bomba atualizar(int id, Bomba bomba) {
        bomba.setId(id);
        validarDadosObrigatorios(bomba);
        return repository.atualizar(bomba);
    }

    public void deletar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException(String.format("ID inválido para exclusão: %d", id));
        }
        repository.deletar(id);
    }

    private void validarDadosObrigatorios(Bomba bomba) {
        if (bomba.getNome() == null || bomba.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da bomba é obrigatório.");
        }
        if (bomba.getCombustivelId() == null || bomba.getCombustivelId() <= 0) {
            throw new IllegalArgumentException("A bomba deve ser associada a um Combustível válido.");
        }
    }
}