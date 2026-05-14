package com.merito.posto.repository;

import com.merito.posto.config.DatabaseConfig;
import com.merito.posto.model.Combustivel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositório para gerenciar acesso a dados da tabela combustivel.
 */
public class CombustivelRepository {
    private static final Logger log = LoggerFactory.getLogger(CombustivelRepository.class);

    public Combustivel salvar(Combustivel combustivel) {
        String sql = "INSERT INTO combustivel (nome, preco_litro) VALUES (?, ?) RETURNING id";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, combustivel.getNome());
            stmt.setDouble(2, combustivel.getPrecoLitro());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    combustivel.setId(rs.getInt("id"));
                    log.info("Combustivel cadastrado com ID: {}", combustivel.getId());
                }
            }
            return combustivel;
        } catch (SQLException e) {
            log.error("Erro ao salvar combustivel: {}", combustivel.getNome(), e);
            throw new RuntimeException("Erro ao salvar combustível no banco de dados.", e);
        }
    }

    public List<Combustivel> listarTodos() {
        String sql = "SELECT id, nome, preco_litro FROM combustivel ORDER BY id";
        List<Combustivel> lista = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Combustivel(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDouble("preco_litro")
                ));
            }
            return lista;
        } catch (SQLException e) {
            log.error("Erro ao listar combustiveis.", e);
            throw new RuntimeException("Erro ao buscar combustíveis no banco de dados.", e);
        }
    }

    public Combustivel atualizar(Combustivel combustivel) {
        String sql = "UPDATE combustivel SET nome = ?, preco_litro = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, combustivel.getNome());
            stmt.setDouble(2, combustivel.getPrecoLitro());
            stmt.setInt(3, combustivel.getId());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new IllegalArgumentException(String.format("Combustível com ID %d não encontrado.", combustivel.getId()));
            }

            log.info("Combustivel atualizado com ID: {}", combustivel.getId());
            return combustivel;
        } catch (SQLException e) {
            log.error("Erro ao atualizar combustivel com ID: {}", combustivel.getId(), e);
            throw new RuntimeException("Erro ao atualizar combustível no banco de dados.", e);
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM combustivel WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new IllegalArgumentException(String.format("Combustível com ID %d não encontrado para exclusão.", id));
            }
            log.info("Combustivel deletado com ID: {}", id);
        } catch (SQLException e) {
            log.error("Erro ao deletar combustivel com ID: {}", id, e);
            throw new RuntimeException("Erro ao deletar combustível no banco de dados. Verifique se ele não está em uso.", e);
        }
    }
}