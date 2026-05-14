package com.merito.posto;

import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        log.info("Iniciando a API do Posto de Combustível...");

        Javalin app = Javalin.create(config -> {
            // Habilita CORS para o nosso Frontend
            config.plugins.enableCors(cors -> {
                cors.add(it -> {
                    it.anyHost();
                });
            });
        }).start(8080);

        app.get("/", ctx -> ctx.result("API do Posto de Combustível rodando!"));

        log.info("API iniciada com sucesso na porta 8080.");
    }
}