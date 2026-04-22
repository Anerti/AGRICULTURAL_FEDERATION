package com.example.agricultural_federation.repositories;

import com.example.agricultural_federation.entities.Cooperative;
import com.example.agricultural_federation.entities.Member;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

@Repository
public class CooperativeRepository {

    private final DataSource dataSource;

    public CooperativeRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

public Cooperative save(Cooperative cooperative, List<Member> members) {
        String sql = """
                        INSERT INTO 
                            agricultural_federation_app.cooperative 
                            (id, name, specialty, number, location, creation_date, status, federation_auth) 
                        VALUES (gen_random_uuid(), ?, ?, ?, ?, ?, ?, ?)
                      """;
        
        try (Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, cooperative.getName());
            stmt.setString(2, cooperative.getSpecialty());
            stmt.setString(3, null);
            stmt.setString(4, cooperative.getLocation());
            stmt.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
            stmt.setString(6, "active");
            stmt.setBoolean(7, cooperative.getFederationApproval() != null ? cooperative.getFederationApproval() : false);
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
        try (Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (Member member : members) {
                stmt.setObject(1, UUID.fromString(coopId));
                stmt.setObject(2, UUID.fromString(member.getId()));
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    public Cooperative findById(String id) {
        String sql = "SELECT id, name, number, specialty, location, creation_date, federation_auth FROM agricultural_federation_app.cooperative WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, UUID.fromString(id));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Cooperative cooperative = new Cooperative();
                cooperative.setId(rs.getString("id"));
                cooperative.setName(rs.getString("name"));
                cooperative.setNumber(rs.getString("number"));
                cooperative.setSpecialty(rs.getString("specialty"));
                cooperative.setLocation(rs.getString("location"));
                cooperative.setFederationApproval(rs.getBoolean("federation_auth"));
                return cooperative;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding cooperative", e);
        }
    }

    public boolean existsByColumn(String column, String value) {
        String sql = "SELECT " + column + " FROM agricultural_federation_app.cooperative WHERE " + column + " = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, value);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Error checking " + column + " existence", e);
        }
    }

    public boolean nameExists(String name) {
        return existsByColumn("name", name);
    }

    public boolean numberExists(String number) {
        return existsByColumn("number", number);
    }

    public String generateNextNumber() {
        String sql = "SELECT COUNT(id) FROM agricultural_federation_app.cooperative";
        try (Connection connection = dataSource.getConnection();
                Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                int count = rs.getInt(1) + 1;
                return String.format("COOP-%03d", count);
            }
            return "COOP-001";
        } catch (SQLException e) {
            throw new RuntimeException("Error generating number", e);
        }
    }

    public Cooperative update(Cooperative cooperative) {
        String sql = "UPDATE agricultural_federation_app.cooperative SET name = ?, number = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cooperative.getName());
            stmt.setString(2, cooperative.getNumber());
            stmt.setObject(3, UUID.fromString(cooperative.getId()));
            stmt.executeUpdate();
            return findById(cooperative.getId());
        } catch (SQLException e) {
            throw new RuntimeException("Error updating cooperative", e);
        }
    }
}
