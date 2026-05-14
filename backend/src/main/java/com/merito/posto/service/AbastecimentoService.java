package com.merito.posto.service;

import com.merito.posto.model.Abastecimento;
import com.merito.posto.repository.AbastecimentoRepository;
import com.merito.posto.repository.BombaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Classe responsável pelas regras de negócio da entidade Abastecimento.
 */
public class AbastecimentoService {

    private final AbastecimentoRepository abastecimentoRepository;
    private final BombaRepository bombaRepository;

    public AbastecimentoService(AbastecimentoRepository abastecimentoRepository, BombaRepository bombaRepository) {
        this.abastecimentoRepository = abastecimentoRepository;
        this.bombaRepository = bombaRepository;
    }

    public Abastecimento criar(Abastecimento abastecimento) {
        preencherValoresPadrao(abastecimento);
        validarDados(abastecimento);
        calcularValorTotalSeNecessario(abastecimento);
        return abastecimentoRepository.salvar(abastecimento);
    }

    public List<Abastecimento> listar() {
        return abastecimentoRepository.listarTodos();
    }

    public Abastecimento atualizar(int id, Abastecimento abastecimento) {
        abastecimento.setId(id);
        preencherValoresPadrao(abastecimento);
        validarDados(abastecimento);
        calcularValorTotalSeNecessario(abastecimento);
        return abastecimentoRepository.atualizar(abastecimento);
    }

    public void deletar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException(String.format("ID inválido para exclusão: %d", id));
        }
        abastecimentoRepository.deletar(id);
    }

    private void preencherValoresPadrao(Abastecimento abastecimento) {
        if (abastecimento.getDataHora() == null) {
            abastecimento.setDataHora(LocalDateTime.now());
        }
    }

    private void validarDados(Abastecimento abastecimento) {
        if (abastecimento.getBombaId() == null || abastecimento.getBombaId() <= 0) {
            throw new IllegalArgumentException("O ID da bomba é obrigatório para registrar um abastecimento.");
        }

        if (!bombaRepository.existe(abastecimento.getBombaId())) {
             throw new IllegalArgumentException("A bomba informada não existe no sistema.");
        }

        if (abastecimento.getLitragem() == null || abastecimento.getLitragem() <= 0) {
            throw new IllegalArgumentException("A litragem deve ser informada e maior que zero.");
        }
    }

    private void calcularValorTotalSeNecessario(Abastecimento abastecimento) {
        // Se o Frontend não enviou o valor total, o Backend calcula multiplicando pelo preco_litro do combustivel da bomba
        if (abastecimento.getValorTotal() == null || abastecimento.getValorTotal() <= 0) {
            Double precoLitro = abastecimentoRepository.buscarPrecoCombustivelDaBomba(abastecimento.getBombaId());
            Double valorCalculado = abastecimento.getLitragem() * precoLitro;
            abastecimento.setValorTotal(valorCalculado);
        }
    }
}