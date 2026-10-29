package com.sanad.model.volunteer;

import com.sanad.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** MODEL: database access (CRUD) for volunteers. */
public class VolunteerRepository {

    private Connection conn() { return DatabaseConnection.getInstance().getConnection(); }

    public void add(Volunteer v) {
        String sql = "INSERT INTO volunteers(name, skills, availability, hours) VALUES(?,?,?,?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, v.getName()); ps.setString(2, v.getSkills());
            ps.setString(3, v.getAvailability()); ps.setDouble(4, v.getHours());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<Volunteer> findAll() {
        List<Volunteer> list = new ArrayList<>();
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM volunteers")) {
            while (rs.next()) {
                list.add(new Volunteer(rs.getInt("id"), rs.getString("name"),
                        rs.getString("skills"), rs.getString("availability"), rs.getDouble("hours")));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    public void update(Volunteer v) {
        String sql = "UPDATE volunteers SET name=?, skills=?, availability=?, hours=? WHERE id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, v.getName()); ps.setString(2, v.getSkills());
            ps.setString(3, v.getAvailability()); ps.setDouble(4, v.getHours());
            ps.setInt(5, v.getId());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void delete(int id) {
        try (PreparedStatement ps = conn().prepareStatement("DELETE FROM volunteers WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}
