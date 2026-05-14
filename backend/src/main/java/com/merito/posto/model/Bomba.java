package com.merito.posto.model;

/**
 * Entidade que representa uma Bomba de Combustível.
 */
public class Bomba {
    private Integer id;
    private String nome;
    private Integer combustivelId;

    // Campo auxiliar para retornar o nome no Frontend sem precisar de joins complexos em todo lugar
    private String combustivelNome;

    public Bomba() {
    }

    public Bomba(Integer id, String nome, Integer combustivelId) {
        this.id = id;
        this.nome = nome;
        this.combustivelId = combustivelId;
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

    public Integer getCombustivelId() {
        return combustivelId;
    }

    public void setCombustivelId(Integer combustivelId) {
        this.combustivelId = combustivelId;
    }

    public String getCombustivelNome() {
        return combustivelNome;
    }

    public void setCombustivelNome(String combustivelNome) {
        this.combustivelNome = combustivelNome;
    }
}