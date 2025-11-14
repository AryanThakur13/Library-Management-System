
package com.library.dao;

import com.library.util.DBConnection;

import java.sql.*;

public class UserDAO {
    public boolean validate(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username=? AND password=?"; // demo only, not hashed
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
