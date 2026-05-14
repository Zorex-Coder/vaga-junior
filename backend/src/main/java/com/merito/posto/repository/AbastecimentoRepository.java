package com.merito.posto.repository;

import com.merito.posto.config.DatabaseConfig;
import com.merito.posto.model.Abastecimento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositório para gerenciar acesso a dados da tabela abastecimento.
 */
public class AbastecimentoRepository {
    private static final Logger log = LoggerFactory.getLogger(AbastecimentoRepository.class);

    public Abastecimento salvar(Abastecimento abastecimento) {
        String sql = "INSERT INTO abastecimento (bomba_id, data_hora, litragem, valor_total) VALUES (?, ?, ?, ?) RETURNING id";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, abastecimento.getBombaId());
            stmt.setTimestamp(2, Timestamp.valueOf(abastecimento.getDataHora()));
            stmt.setDouble(3, abastecimento.getLitragem());
            stmt.setDouble(4, abastecimento.getValorTotal());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    abastecimento.setId(rs.getInt("id"));
                    log.info("Abastecimento registrado com ID: {}", abastecimento.getId());
                }
            }
            return abastecimento;
        } catch (SQLException e) {
            log.error("Erro ao registrar abastecimento na bomba {}", abastecimento.getBombaId(), e);
            throw new RuntimeException("Erro ao salvar abastecimento no banco de dados.", e);
        }
    }

    public List<Abastecimento> listarTodos() {
        // JOIN com bomba para retornar o nome da bomba na lista (facilita o frontend)
        String sql = "SELECT a.id, a.bomba_id, a.data_hora, a.litragem, a.valor_total, b.nome as bomba_nome " +
                     "FROM abastecimento a " +
                     "JOIN bomba b ON a.bomba_id = b.id " +
                     "ORDER BY a.data_hora DESC";
        List<Abastecimento> lista = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Abastecimento ab = new Abastecimento(
                        rs.getInt("id"),
                        rs.getInt("bomba_id"),
                        rs.getTimestamp("data_hora").toLocalDateTime(),
                        rs.getDouble("litragem"),
                        rs.getDouble("valor_total")
                );
                ab.setBombaNome(rs.getString("bomba_nome"));
                lista.add(ab);
            }
            return lista;
        } catch (SQLException e) {
            log.error("Erro ao listar abastecimentos.", e);
            throw new RuntimeException("Erro ao buscar abastecimentos no banco de dados.", e);
        }
    }

    public Abastecimento atualizar(Abastecimento abastecimento) {
        String sql = "UPDATE abastecimento SET bomba_id = ?, data_hora = ?, litragem = ?, valor_total = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, abastecimento.getBombaId());
            stmt.setTimestamp(2, Timestamp.valueOf(abastecimento.getDataHora()));
            stmt.setDouble(3, abastecimento.getLitragem());
            stmt.setDouble(4, abastecimento.getValorTotal());
            stmt.setInt(5, abastecimento.getId());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new IllegalArgumentException(String.format("Abastecimento com ID %d não encontrado.", abastecimento.getId()));
            }

            log.info("Abastecimento atualizado com ID: {}", abastecimento.getId());
            return abastecimento;
        } catch (SQLException e) {
            log.error("Erro ao atualizar abastecimento com ID: {}", abastecimento.getId(), e);
            throw new RuntimeException("Erro ao atualizar abastecimento no banco de dados.", e);
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM abastecimento WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new IllegalArgumentException(String.format("Abastecimento com ID %d não encontrado para exclusão.", id));
            }
            log.info("Abastecimento deletado com ID: {}", id);
        } catch (SQLException e) {
            log.error("Erro ao deletar abastecimento com ID: {}", id, e);
            throw new RuntimeException("Erro ao deletar abastecimento no banco de dados.", e);
        }
    }

    // Busca auxiliar para encontrar o preço do combustível de uma bomba
    public Double buscarPrecoCombustivelDaBomba(int bombaId) {
        String sql = "SELECT c.preco_litro FROM bomba b " +
                     "JOIN combustivel c ON b.combustivel_id = c.id " +
                     "WHERE b.id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bombaId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("preco_litro");
                }
                throw new IllegalArgumentException("Bomba não encontrada ou sem combustível associado.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar preço do combustível.", e);
        }
    }
}