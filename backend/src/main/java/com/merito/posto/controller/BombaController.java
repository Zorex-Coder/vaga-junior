package com.merito.posto.controller;

import com.merito.posto.model.Bomba;
import com.merito.posto.service.BombaService;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador para expor as rotas HTTP relacionadas a Bomba.
 */
public class BombaController {
    private static final Logger log = LoggerFactory.getLogger(BombaController.class);
    private final BombaService service;

    public BombaController(BombaService service) {
        this.service = service;
    }

    public void listar(Context ctx) {
        log.info("Recebida requisição GET para listar bombas");
        ctx.json(service.listar());
    }

    public void criar(Context ctx) {
        log.info("Recebida requisição POST para criar bomba");
        try {
            Bomba bomba = ctx.bodyAsClass(Bomba.class);
            Bomba criada = service.criar(bomba);
            ctx.status(201).json(criada);
        } catch (IllegalArgumentException e) {
            log.warn("Erro de validação ao criar bomba", e);
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            log.error("Erro interno ao criar bomba", e);
            ctx.status(500).result("Erro interno do servidor");
        }
    }

    public void atualizar(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        log.info("Recebida requisição PUT para atualizar bomba com ID: {}", id);

        try {
            Bomba bomba = ctx.bodyAsClass(Bomba.class);
            Bomba atualizada = service.atualizar(id, bomba);
            ctx.json(atualizada);
        } catch (IllegalArgumentException e) {
            log.warn("Erro de validação ao atualizar bomba", e);
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            log.error("Erro interno ao atualizar bomba", e);
            ctx.status(500).result("Erro interno do servidor");
        }
    }

    public void deletar(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        log.info("Recebida requisição DELETE para bomba com ID: {}", id);

        try {
            service.deletar(id);
            ctx.status(204); // No Content
        } catch (IllegalArgumentException e) {
            log.warn("Erro ao deletar bomba: {}", e.getMessage());
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            log.error("Erro interno ao deletar bomba", e);
            ctx.status(500).result("Erro interno do servidor");
        }
    }
}