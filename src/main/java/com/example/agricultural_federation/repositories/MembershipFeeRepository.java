package com.example.agricultural_federation.repositories;

import com.example.agricultural_federation.entities.MembershipFee;
import com.example.agricultural_federation.entities.enums.FrequencyEnum;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class MembershipFeeRepository {

    private final DataSource dataSource;

    public MembershipFeeRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<MembershipFee> findByCooperativeId(String coopId) {
        String sql = "SELECT id, cooperative_id, label, amount, frequency, eligible_form " +
                "FROM agricultural_federation_app.membership_fee " +
                "WHERE cooperative_id = ?";

        List<MembershipFee> fees = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, coopId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                MembershipFee fee = new MembershipFee();
                fee.setId(rs.getString("id"));
                fee.setCooperativeId(rs.getString("cooperative_id"));
                fee.setLabel(rs.getString("label"));
                fee.setAmount(rs.getLong("amount"));
                fee.setFrequency(FrequencyEnum.valueOf(rs.getString("frequency")));
                fee.setEligibleFrom(rs.getTimestamp("eligible_from").toString());
                fees.add(fee);
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error fetching membership fees", e);
        }
        return fees;
    }

    public MembershipFee findById(UUID feeId) {
        String sql = "SELECT id, cooperative_id, label, amount, frequency, eligible_from " +
                "FROM agricultural_federation_app.membership_fee WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, feeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                MembershipFee fee = new MembershipFee();
                fee.setId(rs.getString("id"));
                fee.setCooperativeId(rs.getString("cooperative_id"));
                fee.setLabel(rs.getString("label"));
                fee.setAmount(rs.getLong("amount"));
                fee.setFrequency(FrequencyEnum.valueOf(rs.getString("frequency")));
                fee.setEligibleFrom(rs.getTimestamp("eligible_from").toString());
                return fee;
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB error fetching membership fee", e);
        }
        return null;
    }
}

