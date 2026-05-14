package com.merito.posto.repository;

import com.merito.posto.config.DatabaseConfig;
import com.merito.posto.model.Bomba;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositório para gerenciar acesso a dados da tabela bomba.
 */
public class BombaRepository {
    private static final Logger log = LoggerFactory.getLogger(BombaRepository.class);

    public Bomba salvar(Bomba bomba) {
        String sql = "INSERT INTO bomba (nome, combustivel_id) VALUES (?, ?) RETURNING id";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, bomba.getNome());
            stmt.setInt(2, bomba.getCombustivelId());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    bomba.setId(rs.getInt("id"));
                    log.info("Bomba cadastrada com ID: {}", bomba.getId());
                }
            }
            return bomba;
        } catch (SQLException e) {
            log.error("Erro ao salvar bomba: {}", bomba.getNome(), e);
            throw new RuntimeException("Erro ao salvar bomba no banco de dados.", e);
        }
    }

    public List<Bomba> listarTodos() {
        // Faz um JOIN para já buscar o nome do combustível e facilitar a exibição no Frontend
        String sql = "SELECT b.id, b.nome, b.combustivel_id, c.nome as combustivel_nome " +
                     "FROM bomba b " +
                     "JOIN combustivel c ON b.combustivel_id = c.id " +
                     "ORDER BY b.id";
        List<Bomba> lista = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Bomba bomba = new Bomba(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getInt("combustivel_id")
                );
                bomba.setCombustivelNome(rs.getString("combustivel_nome"));
                lista.add(bomba);
            }
            return lista;
        } catch (SQLException e) {
            log.error("Erro ao listar bombas.", e);
            throw new RuntimeException("Erro ao buscar bombas no banco de dados.", e);
        }
    }

    public Bomba atualizar(Bomba bomba) {
        String sql = "UPDATE bomba SET nome = ?, combustivel_id = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, bomba.getNome());
            stmt.setInt(2, bomba.getCombustivelId());
            stmt.setInt(3, bomba.getId());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new IllegalArgumentException(String.format("Bomba com ID %d não encontrada.", bomba.getId()));
            }

            log.info("Bomba atualizada com ID: {}", bomba.getId());
            return bomba;
        } catch (SQLException e) {
            log.error("Erro ao atualizar bomba com ID: {}", bomba.getId(), e);
            throw new RuntimeException("Erro ao atualizar bomba no banco de dados.", e);
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM bomba WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new IllegalArgumentException(String.format("Bomba com ID %d não encontrada para exclusão.", id));
            }
            log.info("Bomba deletada com ID: {}", id);
        } catch (SQLException e) {
            log.error("Erro ao deletar bomba com ID: {}", id, e);
            throw new RuntimeException("Erro ao deletar bomba no banco de dados. Verifique se ela não possui abastecimentos vinculados.", e);
        }
    }

    public boolean existe(int id) {
        String sql = "SELECT 1 FROM bomba WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar existência da bomba.", e);
        }
    }
}