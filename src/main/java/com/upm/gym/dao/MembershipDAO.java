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
}