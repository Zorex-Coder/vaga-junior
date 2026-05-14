package com.merito.posto.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gerencia a conexão com o banco de dados PostgreSQL.
 */
public class DatabaseConfig {
    private static final Logger log = LoggerFactory.getLogger(DatabaseConfig.class);
    private static HikariDataSource dataSource;

    /**
     * Inicializa o pool de conexões.
     */
    public static void init() {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:postgresql://localhost:5432/posto_combustivel");
            config.setUsername("admin");
            config.setPassword("adminpassword");
            config.setMaximumPoolSize(10);

            dataSource = new HikariDataSource(config);
            log.info("Pool de conexões com o banco de dados iniciado.");

            createTables();
        } catch (Exception e) {
            log.error("Erro ao inicializar o banco de dados", e);
            throw new RuntimeException("Falha ao conectar no banco de dados.", e);
        }
    }

    /**
     * Fornece uma conexão ativa do pool.
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new IllegalStateException("DatabaseConfig não foi inicializado.");
        }
        return dataSource.getConnection();
    }

    /**
     * Cria as tabelas necessárias caso não existam.
     */
    private static void createTables() {
        String sqlCombustivel = "CREATE TABLE IF NOT EXISTS combustivel (" +
                "id SERIAL PRIMARY KEY, " +
                "nome VARCHAR(100) NOT NULL, " +
                "preco_litro DECIMAL(10,2) NOT NULL" +
                ");";

        String sqlBomba = "CREATE TABLE IF NOT EXISTS bomba (" +
                "id SERIAL PRIMARY KEY, " +
                "nome VARCHAR(100) NOT NULL, " +
                "combustivel_id INT NOT NULL, " +
                "FOREIGN KEY (combustivel_id) REFERENCES combustivel(id)" +
                ");";

        String sqlAbastecimento = "CREATE TABLE IF NOT EXISTS abastecimento (" +
                "id SERIAL PRIMARY KEY, " +
                "bomba_id INT NOT NULL, " +
                "data_hora TIMESTAMP NOT NULL, " +
                "litragem DECIMAL(10,2) NOT NULL, " +
                "valor_total DECIMAL(10,2) NOT NULL, " +
                "FOREIGN KEY (bomba_id) REFERENCES bomba(id)" +
                ");";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sqlCombustivel);
            stmt.execute(sqlBomba);
            stmt.execute(sqlAbastecimento);
            log.info("Tabelas do banco de dados verificadas/criadas com sucesso.");
        } catch (SQLException e) {
            log.error("Erro ao criar tabelas no banco de dados", e);
            throw new RuntimeException("Falha ao criar tabelas.", e);
        }
    }
}