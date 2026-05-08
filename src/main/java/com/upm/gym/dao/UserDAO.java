package com.upm.gym.dao;

import com.upm.gym.enums.Role;
import com.upm.gym.exception.DatabaseException;
import com.upm.gym.model.User;
import com.upm.gym.util.DBConnection;

import java.sql.*;

public class UserDAO {

    public User login(String userId, String password) {

        String sql =
                "SELECT * FROM users " +
                "WHERE user_id=? AND password=?";

        try (Connection conn =
                     DBConnection.getConnection();

             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                return new User(
                        rs.getString("user_id"),
                        rs.getString("full_name"),
                        rs.getString("password"),
                        Role.valueOf(
                                rs.getString("role"))
                );
            }

        } catch (SQLException e) {

            throw new DatabaseException(
                    "Error during login."
            );
        }

        return null;
    }
}