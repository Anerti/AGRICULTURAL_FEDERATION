package com.example.agricultural_federation.repositories;

import com.example.agricultural_federation.entities.Member;
import com.example.agricultural_federation.entities.enums.GenderEnum;
import com.example.agricultural_federation.entities.enums.RoleEnum;
import com.example.agricultural_federation.exceptions.NotFoundException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Repository
public class MemberRepository {

    private final Connection connection;

    public MemberRepository(Connection connection) {
        this.connection = connection;
    }

    public Member findById(String id) {
        String sql = """
            SELECT 
                id, 
                first_name, 
                last_name, 
                birth_date, 
                gender, 
                address, 
                phone, 
                profession, 
                email, 
                role, 
                joined_at, 
                coop_id, 
                status 
            FROM 
                agricultural_federation_app.member 
            WHERE 
                id = ?""";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, UUID.fromString(id));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToMember(rs);
            }
            throw new NotFoundException("Member not found with id: " + id);
        } catch (SQLException e) {
            throw new RuntimeException("Error finding member", e);
        }
    }

    private Member mapResultSetToMember(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setId(rs.getString("id"));
        member.setFirstName(rs.getString("first_name"));
        member.setLastName(rs.getString("last_name"));
        member.setBirthDate(rs.getDate("birth_date"));
        String gender = rs.getString("gender");
        if (gender != null) {
            member.setGender(GenderEnum.valueOf(gender));
        }
        member.setAddress(rs.getString("address"));
        member.setPhoneNumber(rs.getString("phone"));
        member.setProfession(rs.getString("profession"));
        member.setEmail(rs.getString("email"));
        member.setJoinedAt(rs.getTimestamp("joined_at"));
        String role = rs.getString("role");
        if (role != null) {
            member.setRole(RoleEnum.valueOf(role));
        }
        return member;
    }
}
