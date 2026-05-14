package com.merito.posto.service;

import com.merito.posto.model.Combustivel;
import com.merito.posto.repository.CombustivelRepository;

import java.util.List;

/**
 * Classe responsável pelas regras de negócio da entidade Combustível.
 */
public class CombustivelService {

    private final CombustivelRepository repository;

    // Injeção de dependência via construtor (Regra do ia.md)
    public CombustivelService(CombustivelRepository repository) {
        this.repository = repository;
    }

    public Combustivel criar(Combustivel combustivel) {
        validarDadosObrigatorios(combustivel);
        return repository.salvar(combustivel);
    }

    public List<Combustivel> listar() {
        return repository.listarTodos();
    }

    public Combustivel atualizar(int id, Combustivel combustivel) {
        combustivel.setId(id);
        validarDadosObrigatorios(combustivel);
        return repository.atualizar(combustivel);
    }

    public void deletar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException(String.format("ID inválido para exclusão: %d", id));
        }
        repository.deletar(id);
    }

    private void validarDadosObrigatorios(Combustivel combustivel) {
        if (combustivel.getNome() == null || combustivel.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do combustível é obrigatório (Formato esperado: String não vazia).");
        }
        if (combustivel.getPrecoLitro() == null || combustivel.getPrecoLitro() <= 0) {
            throw new IllegalArgumentException(String.format("O preço por litro é inválido: %s (Formato esperado: Número maior que zero).", combustivel.getPrecoLitro()));
        }
    }
}