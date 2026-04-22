package com.example.agricultural_federation.repositories;

import com.example.agricultural_federation.entities.Member;
import com.example.agricultural_federation.entities.enums.GenderEnum;
import com.example.agricultural_federation.entities.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final DataSource dataSource;
    private static final String MEMBER_COLUMNS =
            "id, first_name, last_name, birth_date, gender, address, " +
                    "profession, phone, email, occupation, joined_at, coop_id";

    public Optional<Member> findById(String id) throws SQLException {
        String sql = "SELECT " + MEMBER_COLUMNS + " FROM agricultural_federation_db.member WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, java.util.UUID.fromString(id));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Member> findAllByIds(List<String> ids) throws SQLException {
        if (ids == null || ids.isEmpty()) return Collections.emptyList();

        String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));
        String sql = "SELECT " + MEMBER_COLUMNS + " FROM member WHERE id IN (" + placeholders + ")";
        List<Member> result = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < ids.size(); i++) {
                ps.setObject(i + 1, java.util.UUID.fromString(ids.get(i)));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        }
        return result;
    }

    public Member save(Member member) throws SQLException {
        String sql = """
                INSERT INTO agricultural_federation_db.member
                  (id, first_name, last_name, birth_date, gender, address,
                   profession, phone, email, occupation, joined_at, coop_id, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'active')
                """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, java.util.UUID.fromString(member.getId()));
            ps.setString(2, member.getFirstName());
            ps.setString(3, member.getLastName());
            ps.setDate(4, new java.sql.Date(member.getBirthDate().getTime()));
            ps.setString(5, member.getGender().name());
            ps.setString(6, member.getAddress());
            ps.setString(7, member.getProfession());
            ps.setString(8, member.getPhoneNumber());
            ps.setString(9, member.getEmail());
            ps.setString(10, member.getRole() != null ? member.getRole().name() : RoleEnum.JUNIOR.name());
            ps.setTimestamp(11, new Timestamp(member.getJoinedAt().getTime()));
            if (member.getCollectivityId() != null) {
                ps.setObject(12, java.util.UUID.fromString(member.getCollectivityId()));
            } else {
                ps.setNull(12, Types.OTHER);
            }
            ps.executeUpdate();
        }
        return member;
    }

    public void saveReferrers(String newMemberId, List<String> referrerIds) throws SQLException {
        if (referrerIds == null || referrerIds.isEmpty()) return;
        String sql = """
                INSERT INTO agricultural_federation_db.referrer (id, referrer_at, new_member, referrer_id)
                VALUES (gen_random_uuid(), now(), ?, ?)
                """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (String referrerId : referrerIds) {
                ps.setObject(1, java.util.UUID.fromString(newMemberId));
                ps.setObject(2, java.util.UUID.fromString(referrerId));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private Member mapRow(ResultSet rs) throws SQLException {
        Member m = new Member();
        m.setId(rs.getString("id"));
        m.setFirstName(rs.getString("first_name"));
        m.setLastName(rs.getString("last_name"));
        m.setBirthDate(rs.getDate("birth_date"));
        m.setGender(GenderEnum.valueOf(rs.getString("gender")));
        m.setAddress(rs.getString("address"));
        m.setProfession(rs.getString("profession"));
        m.setPhoneNumber(rs.getString("phone"));
        m.setEmail(rs.getString("email"));
        String occ = rs.getString("occupation");
        if (occ != null) m.setRole(RoleEnum.valueOf(occ));
        m.setJoinedAt(rs.getTimestamp("joined_at"));
        m.setCollectivityId(rs.getString("coop_id"));
        return m;
    }
}