package com.sanad.model.lookup;

import com.sanad.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** MODEL: database access (full CRUD) for lookups. */
public class LookupRepository {

    private Connection conn() {
        return DatabaseConnection.getInstance().getConnection();   // Singleton
    }

    // CREATE
    public void add(Lookup x) {
        String sql = "INSERT INTO lookups(kind, name) VALUES(?, ?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, x.getKind());
            ps.setString(2, x.getName());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // READ
    public List<Lookup> findAll() {
        List<Lookup> list = new ArrayList<>();
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM lookups")) {
            while (rs.next()) {
                list.add(new Lookup(rs.getInt("id"), rs.getString("kind"), rs.getString("name")));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    // UPDATE
    public void update(Lookup x) {
        String sql = "UPDATE lookups SET kind=?, name=? WHERE id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, x.getKind());
            ps.setString(2, x.getName());
            ps.setInt(3, x.getId());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // DELETE
    public void delete(int id) {
        try (PreparedStatement ps = conn().prepareStatement("DELETE FROM lookups WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}
