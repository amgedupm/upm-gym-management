package com.upm.gym.dao;

import com.upm.gym.exception.DatabaseException;
import com.upm.gym.model.Membership;
import com.upm.gym.util.DBConnection;

import java.sql.*;

public class MembershipDAO {

    public void saveMembership(
            Membership membership) {

        String sql =
                "INSERT INTO memberships " +
                "(user_id, membership_type, " +
                "start_date, expiry_date, auto_renew) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn =
                     DBConnection.getConnection();

             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setString(1,
                    membership.getUserId());

            stmt.setString(2,
                    membership.getMembershipType());

            stmt.setDate(3,
                    Date.valueOf(
                            membership.getStartDate()));

            stmt.setDate(4,
                    Date.valueOf(
                            membership.getExpiryDate()));

            stmt.setBoolean(5,
                    membership.isAutoRenew());

            stmt.executeUpdate();

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Failed to save membership."
            );
        }
    }
    public Membership getActiveMembershipByUserId(
        String userId) {

    String sql =
            "SELECT * FROM memberships " +
            "WHERE user_id = ? " +
            "AND expiry_date >= CURDATE() " +
            "AND status = 'ACTIVE' " +
            "ORDER BY expiry_date DESC " +
            "LIMIT 1";

    try (Connection conn =
                 DBConnection.getConnection();

         PreparedStatement stmt =
                 conn.prepareStatement(sql)) {

        stmt.setString(1, userId);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {

            return new Membership(
                    rs.getInt("membership_id"),
                    rs.getString("user_id"),
                    rs.getString("membership_type"),
                    rs.getDate("start_date")
                            .toLocalDate(),
                    rs.getDate("expiry_date")
                            .toLocalDate(),
                    rs.getBoolean("auto_renew")
            );
        }

    } catch (SQLException e) {

        throw new DatabaseException(
                "Failed to retrieve membership."
        );
    }

    return null;
    }
    public boolean cancelMembership(
        int membershipId) {

    String sql =
            "UPDATE memberships " +
            "SET status = 'CANCELLED' " +
            "WHERE membership_id = ?";

    try (Connection conn =
                 DBConnection.getConnection();

         PreparedStatement stmt =
                 conn.prepareStatement(sql)) {

        stmt.setInt(1, membershipId);

        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {

        throw new DatabaseException(
                "Failed to cancel membership."
        );
    }
    }
    public int countActiveMemberships() {

    String sql =
            "SELECT COUNT(*) " +
            "FROM memberships " +
            "WHERE expiry_date >= CURDATE() " +
            "AND status = 'ACTIVE'";

    try (Connection conn =
                 DBConnection.getConnection();

         PreparedStatement stmt =
                 conn.prepareStatement(sql);

         ResultSet rs =
                 stmt.executeQuery()) {

        if (rs.next()) {

            return rs.getInt(1);
        }

    } catch (SQLException e) {

        throw new DatabaseException(
                "Failed to count memberships."
        );
    }

    return 0;
    }
    public int countTotalMembers() {

    String sql =
            "SELECT COUNT(*) FROM users " +
            "WHERE role IN ('STUDENT', 'FACULTY')";

    try (Connection conn =
                 DBConnection.getConnection();

         PreparedStatement stmt =
                 conn.prepareStatement(sql);

         ResultSet rs =
                 stmt.executeQuery()) {

        if (rs.next()) {

            return rs.getInt(1);
        }

    } catch (SQLException e) {

        throw new DatabaseException(
                "Failed to count users."
        );
    }

    return 0;
    }
}