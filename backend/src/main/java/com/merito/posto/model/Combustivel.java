package com.merito.posto.model;

/**
 * Entidade que representa um Tipo de Combustível.
 */
public class Combustivel {
    private Integer id;
    private String nome;
    private Double precoLitro;

    public Combustivel() {
    }

    public Combustivel(Integer id, String nome, Double precoLitro) {
        this.id = id;
        this.nome = nome;
        this.precoLitro = precoLitro;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPrecoLitro() {
        return precoLitro;
    }

    public void setPrecoLitro(Double precoLitro) {
        this.precoLitro = precoLitro;
    }
}