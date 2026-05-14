package com.merito.posto.model;

import java.time.LocalDateTime;

/**
 * Entidade que representa o registro de um Abastecimento.
 */
public class Abastecimento {
    private Integer id;
    private Integer bombaId;
    private LocalDateTime dataHora;
    private Double litragem;
    private Double valorTotal;

    // Campo auxiliar para a UI
    private String bombaNome;

    public Abastecimento() {
    }

    public Abastecimento(Integer id, Integer bombaId, LocalDateTime dataHora, Double litragem, Double valorTotal) {
        this.id = id;
        this.bombaId = bombaId;
        this.dataHora = dataHora;
        this.litragem = litragem;
        this.valorTotal = valorTotal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBombaId() {
        return bombaId;
    }

    public void setBombaId(Integer bombaId) {
        this.bombaId = bombaId;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Double getLitragem() {
        return litragem;
    }

    public void setLitragem(Double litragem) {
        this.litragem = litragem;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getBombaNome() {
        return bombaNome;
    }

    public void setBombaNome(String bombaNome) {
        this.bombaNome = bombaNome;
    }
}