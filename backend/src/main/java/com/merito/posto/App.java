package com.merito.posto;

import com.merito.posto.config.DatabaseConfig;
import com.merito.posto.controller.CombustivelController;
import com.merito.posto.repository.CombustivelRepository;
import com.merito.posto.service.CombustivelService;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        log.info("Iniciando a API do Posto de Combustível...");

        // Inicializa o banco de dados e cria as tabelas
        DatabaseConfig.init();

        // Injeção de dependência manual (como não usamos Spring)
        CombustivelRepository combustivelRepository = new CombustivelRepository();
        CombustivelService combustivelService = new CombustivelService(combustivelRepository);
        CombustivelController combustivelController = new CombustivelController(combustivelService);

        Javalin app = Javalin.create(config -> {
            // Habilita CORS para o nosso Frontend
            config.plugins.enableCors(cors -> {
                cors.add(it -> {
                    it.anyHost();
                });
            });
        }).start(8080);

        app.get("/", ctx -> ctx.result("API do Posto de Combustível rodando!"));

        // Rotas de Combustível
        app.get("/api/combustiveis", combustivelController::listar);
        app.post("/api/combustiveis", combustivelController::criar);
        app.put("/api/combustiveis/{id}", combustivelController::atualizar);
        app.delete("/api/combustiveis/{id}", combustivelController::deletar);

        log.info("API iniciada com sucesso na porta 8080.");
    }
}