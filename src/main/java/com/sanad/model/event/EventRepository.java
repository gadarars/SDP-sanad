package com.sanad.model.event;

import com.sanad.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** MODEL: database access (CRUD) for events. */
public class EventRepository {

    private Connection conn() { return DatabaseConnection.getInstance().getConnection(); }

    public void add(Event e) {
        String sql = "INSERT INTO events(name, date, location, category) VALUES(?,?,?,?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, e.getName()); ps.setString(2, e.getDate());
            ps.setString(3, e.getLocation()); ps.setString(4, e.getCategory());
            ps.executeUpdate();
        } catch (SQLException ex) { throw new RuntimeException(ex); }
    }

    public List<Event> findAll() {
        List<Event> list = new ArrayList<>();
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM events")) {
            while (rs.next()) {
                list.add(new Event(rs.getInt("id"), rs.getString("name"),
                        rs.getString("date"), rs.getString("location"), rs.getString("category")));
            }
        } catch (SQLException ex) { throw new RuntimeException(ex); }
        return list;
    }

    public void update(Event e) {
        String sql = "UPDATE events SET name=?, date=?, location=?, category=? WHERE id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, e.getName()); ps.setString(2, e.getDate());
            ps.setString(3, e.getLocation()); ps.setString(4, e.getCategory());
            ps.setInt(5, e.getId());
            ps.executeUpdate();
        } catch (SQLException ex) { throw new RuntimeException(ex); }
    }

    public void delete(int id) {
        try (PreparedStatement ps = conn().prepareStatement("DELETE FROM events WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) { throw new RuntimeException(ex); }
    }
}
