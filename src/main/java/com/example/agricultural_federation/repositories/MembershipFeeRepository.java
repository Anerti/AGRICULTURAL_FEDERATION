package com.example.agricultural_federation.repositories;

import com.example.agricultural_federation.entities.MembershipFee;
import com.example.agricultural_federation.entities.enums.ActivityStatus;
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

    public List<MembershipFee> findByCooperativeId(String cooperativeId) {
        String sql = "SELECT id, cooperative_id, label, status, frequency, amount, eligible_from FROM agricultural_federation_app.membership_fee WHERE cooperative_id = ?::uuid";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cooperativeId);
            ResultSet rs = ps.executeQuery();
            List<MembershipFee> fees = new ArrayList<>();
            while (rs.next()) {
                fees.add(mapRow(rs));
            }
            return fees;
        } catch (SQLException e) {
            throw new RuntimeException("DB error fetching fees", e);
        }
    }

    public MembershipFee save(MembershipFee fee) {
        String sql = "INSERT INTO agricultural_federation_app.membership_fee " +
                "(cooperative_id, label, status, frequency, amount, eligible_from) " +
                "VALUES (?::uuid, ?, ?::agricultural_federation_app.activity_status, " +
                "        ?::agricultural_federation_app.frequency_enum, ?, ?) " +
                "RETURNING id, cooperative_id, label, status, frequency, amount, eligible_from";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fee.getCooperativeId());
            ps.setString(2, fee.getLabel());
            ps.setString(3, String.valueOf(fee.getStatus()));
            ps.setString(4, String.valueOf(fee.getFrequency()));
            ps.setLong(5, fee.getAmount());
            ps.setString(6, fee.getEligibleFrom());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                fee.setId(rs.getString("id"));
            }
            return fee;
        } catch (SQLException e) {
            throw new RuntimeException("DB error saving fee", e);
        }
    }

    private MembershipFee mapRow(ResultSet rs) throws SQLException {
        MembershipFee fee = new MembershipFee();
        fee.setId(rs.getString("id"));
        fee.setCooperativeId(rs.getString("cooperative_id"));
        fee.setLabel(rs.getString("label"));
        fee.setStatus(ActivityStatus.valueOf(rs.getString("status")));
        fee.setFrequency(FrequencyEnum.valueOf(rs.getString("frequency")));
        fee.setAmount(rs.getLong("amount"));
        fee.setEligibleFrom(rs.getString("eligible_from"));
        return fee;
    }
}
