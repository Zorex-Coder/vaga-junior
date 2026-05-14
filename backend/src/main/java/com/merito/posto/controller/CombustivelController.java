package com.merito.posto.controller;

import com.merito.posto.model.Combustivel;
import com.merito.posto.service.CombustivelService;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador para expor as rotas HTTP relacionadas a Combustível.
 */
public class CombustivelController {
    private static final Logger log = LoggerFactory.getLogger(CombustivelController.class);
    private final CombustivelService service;

    // Injeção de dependência via construtor
    public CombustivelController(CombustivelService service) {
        this.service = service;
    }

    public void listar(Context ctx) {
        log.info("Recebida requisição GET para listar combustíveis");
        ctx.json(service.listar());
    }

    public void criar(Context ctx) {
        log.info("Recebida requisição POST para criar combustível");
        try {
            Combustivel combustivel = ctx.bodyAsClass(Combustivel.class);
            Combustivel criado = service.criar(combustivel);
            ctx.status(201).json(criado);
        } catch (IllegalArgumentException e) {
            log.warn("Erro de validação ao criar combustível", e);
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            log.error("Erro interno ao criar combustível", e);
            ctx.status(500).result("Erro interno do servidor");
        }
    }

    public void atualizar(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        log.info("Recebida requisição PUT para atualizar combustível com ID: {}", id);

        try {
            Combustivel combustivel = ctx.bodyAsClass(Combustivel.class);
            Combustivel atualizado = service.atualizar(id, combustivel);
            ctx.json(atualizado);
        } catch (IllegalArgumentException e) {
            log.warn("Erro de validação ao atualizar combustível", e);
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            log.error("Erro interno ao atualizar combustível", e);
            ctx.status(500).result("Erro interno do servidor");
        }
    }

    public void deletar(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        log.info("Recebida requisição DELETE para combustível com ID: {}", id);

        try {
            service.deletar(id);
            ctx.status(204); // No Content
        } catch (IllegalArgumentException e) {
            log.warn("Erro ao deletar combustível: {}", e.getMessage());
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            log.error("Erro interno ao deletar combustível", e);
            ctx.status(500).result("Erro interno do servidor (Pode estar em uso por uma bomba)");
        }
    }
}