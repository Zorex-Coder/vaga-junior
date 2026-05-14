package com.merito.posto.controller;

import com.merito.posto.model.Abastecimento;
import com.merito.posto.service.AbastecimentoService;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador para expor as rotas HTTP relacionadas a Abastecimentos.
 */
public class AbastecimentoController {
    private static final Logger log = LoggerFactory.getLogger(AbastecimentoController.class);
    private final AbastecimentoService service;

    public AbastecimentoController(AbastecimentoService service) {
        this.service = service;
    }

    public void listar(Context ctx) {
        log.info("Recebida requisição GET para listar abastecimentos");
        ctx.json(service.listar());
    }

    public void criar(Context ctx) {
        log.info("Recebida requisição POST para criar abastecimento");
        try {
            Abastecimento abastecimento = ctx.bodyAsClass(Abastecimento.class);
            Abastecimento criado = service.criar(abastecimento);
            ctx.status(201).json(criado);
        } catch (IllegalArgumentException e) {
            log.warn("Erro de validação ao criar abastecimento", e);
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            log.error("Erro interno ao criar abastecimento", e);
            ctx.status(500).result("Erro interno do servidor");
        }
    }

    public void atualizar(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        log.info("Recebida requisição PUT para atualizar abastecimento com ID: {}", id);

        try {
            Abastecimento abastecimento = ctx.bodyAsClass(Abastecimento.class);
            Abastecimento atualizado = service.atualizar(id, abastecimento);
            ctx.json(atualizado);
        } catch (IllegalArgumentException e) {
            log.warn("Erro de validação ao atualizar abastecimento", e);
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            log.error("Erro interno ao atualizar abastecimento", e);
            ctx.status(500).result("Erro interno do servidor");
        }
    }

    public void deletar(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        log.info("Recebida requisição DELETE para abastecimento com ID: {}", id);

        try {
            service.deletar(id);
            ctx.status(204); // No Content
        } catch (IllegalArgumentException e) {
            log.warn("Erro ao deletar abastecimento: {}", e.getMessage());
            ctx.status(400).result(e.getMessage());
        } catch (Exception e) {
            log.error("Erro interno ao deletar abastecimento", e);
            ctx.status(500).result("Erro interno do servidor");
        }
    }
}