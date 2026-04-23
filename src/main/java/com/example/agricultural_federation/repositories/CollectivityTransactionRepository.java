package com.example.agricultural_federation.repositories;

import com.example.agricultural_federation.entities.CollectivityTransaction;
import com.example.agricultural_federation.entities.Member;
import com.example.agricultural_federation.entities.enums.PaymentMode;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CollectivityTransactionRepository {

    private final DataSource dataSource;

    public CollectivityTransactionRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public CollectivityTransaction save(CollectivityTransaction transaction) {
        String sql = "INSERT INTO agricultural_federation_app.collectivity_transaction " +
                "(cooperative_id, creation_date, amount, payment_mode, account_credited_identifier, member_debited_id) " +
                "VALUES (?::uuid, ?, ?, ?::agricultural_federation_app.payment_mode, ?::uuid, ?::uuid) " +
                "RETURNING id, creation_date";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, transaction.getId());
            ps.setTimestamp(2, transaction.getCreationDate() != null ?
                    Timestamp.valueOf(transaction.getCreationDate()) : new Timestamp(System.currentTimeMillis()));
            ps.setLong(3, transaction.getAmount());
            ps.setString(4, String.valueOf(transaction.getPaymentMode()));
            ps.setString(5, transaction.getAccountCredited() != null ?
                    transaction.getAccountCredited().getId() : null);
            ps.setObject(6, transaction.getMemberDebited() != null ?
                    transaction.getMemberDebited().getId() : null);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                transaction.setId(rs.getString("id"));
                transaction.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
            }
            return transaction;

        } catch (SQLException e) {
            throw new RuntimeException("DB error saving collectivity transaction: " + e.getMessage(), e);
        }
    }

    public List<CollectivityTransaction> findByCooperativeIdAndDateRange(String cooperativeId, Timestamp from, Timestamp to) {
        String sql = "SELECT id, creation_date, amount, payment_mode, account_credited_identifier, member_debited_id " +
                "FROM agricultural_federation_app.collectivity_transaction " +
                "WHERE cooperative_id = ?::uuid AND creation_date >= ? AND creation_date <= ? " +
                "ORDER BY creation_date DESC";

        List<CollectivityTransaction> transactions = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, cooperativeId);
            ps.setTimestamp(2, from);
            ps.setTimestamp(3, to);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CollectivityTransaction tx = new CollectivityTransaction();
                tx.setId(rs.getString("id"));
                tx.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime());
                tx.setAmount(rs.getLong("amount"));
                tx.setPaymentMode(PaymentMode.valueOf(rs.getString("payment_mode")));
                transactions.add(tx);
            }
            return transactions;

        } catch (SQLException e) {
            throw new RuntimeException("DB error fetching transactions: " + e.getMessage(), e);
        }
    }
}