package com.sanad.model.user;

import com.sanad.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** MODEL: database access (CRUD) for users. */
public class UserRepository {

    public void add(User u) {
        String sql = "INSERT INTO users(username, email, role) VALUES(?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setString(1, u.getUsername()); ps.setString(2, u.getEmail()); ps.setString(3, u.getRole());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        try (Statement st = DatabaseConnection.getInstance().getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM users")) {
            while (rs.next()) {
                list.add(new User(rs.getInt("id"), rs.getString("username"),
                        rs.getString("email"), rs.getString("role")));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    public void update(User u) {
        String sql = "UPDATE users SET username=?, email=?, role=? WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setString(1, u.getUsername()); ps.setString(2, u.getEmail());
            ps.setString(3, u.getRole()); ps.setInt(4, u.getId());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void delete(int id) {
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement("DELETE FROM users WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}
