package com.example.agricultural_federation.repositories;

import com.example.agricultural_federation.entities.Cooperative;
import com.example.agricultural_federation.entities.Member;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

@Repository
public class CooperativeRepository {

    private final Connection connection;

    public CooperativeRepository(Connection connection) {
        this.connection = connection;
    }

    public Cooperative save(Cooperative cooperative, List<Member> members) {
        String sql = """
                        INSERT INTO 
                            agricultural_federation_app.cooperative 
                            (DEFAULT, name, specialty, DEFAULT, location, DEFAULT, status, federation_auth) 
                        VALUES (?, ?, ?, ?, ?)
                     """;
        
        try (
                PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, cooperative.getName());
            stmt.setString(2, cooperative.getSpecialty());
            stmt.setString(3, cooperative.getLocation());
            stmt.setString(4, "active");
            stmt.setBoolean(5, cooperative.getFederationApproval() != null ? cooperative.getFederationApproval() : false);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                cooperative.setId(rs.getString("id"));
            }

            updateMembersCoopId(cooperative.getId(), members);

            return cooperative;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving cooperative", e);
        }
    }

    private void updateMembersCoopId(String coopId, List<Member> members) throws SQLException {
        String sql = "UPDATE agricultural_federation_app.member SET coop_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (Member member : members) {
                stmt.setObject(1, UUID.fromString(coopId));
                stmt.setObject(2, UUID.fromString(member.getId()));
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }
}
