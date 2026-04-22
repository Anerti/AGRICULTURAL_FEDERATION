package com.example.agricultural_federation.repositories;

import com.example.agricultural_federation.entities.Payment;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;

@Repository
public class PaymentRepository {

    private final DataSource dataSource;

    public PaymentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Payment save(Payment payment) {
        String sql = "INSERT INTO agricultural_federation_app.payment " +
                "(member_id, membership_fee_id, account_credited_id, amount, payment_mode, creation_date) " +
                "VALUES (?, ?::uuid, ?::uuid, ?, ?::agricultural_federation_app.payment_mode, CURRENT_DATE) " +
                "RETURNING id, member_id, membership_fee_id, account_credited_id, " +
                "          amount, payment_mode, creation_date, created_at";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, payment.getMemberId());
            ps.setString(2, payment.getMembershipFeeIdentifier());
            ps.setString(3, payment.getAccountCreditedIdentifier());
            ps.setLong(4, payment.getAmount());
            ps.setString(5, String.valueOf(payment.getPaymentMode()));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                payment.setId(rs.getString("id"));
                payment.setCreationDate(rs.getDate("creation_date").toLocalDate());
                payment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }
            return payment;

        } catch (SQLException e) {
            throw new RuntimeException("DB error saving payment: " + e.getMessage(), e);
        }
    }

    public String findCoopIdByFeeIdentifier(String feeIdentifier) {
        String sql = "SELECT cooperativeId FROM agricultural_federation_app.membership_fee WHERE id = ?::uuid";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, feeIdentifier);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("cooperativeId");
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("DB error fetching fee: " + e.getMessage(), e);
        }
    }

    public boolean accountExistsForCoop(String accountIdentifier, String cooperativeId) {
        String sql = "SELECT id FROM agricultural_federation_app.financial_account " +
                "WHERE id = ?::uuid AND cooperativeId = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountIdentifier);
            ps.setObject(2, cooperativeId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("DB error checking account: " + e.getMessage(), e);
        }
    }
}


