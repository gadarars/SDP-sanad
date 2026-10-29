package com.sanad.model.donation;

import com.sanad.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** MODEL: database access (full CRUD) for donations. */
public class DonationRepository {

    private Connection conn() {
        return DatabaseConnection.getInstance().getConnection();   // Singleton
    }

    // CREATE
    public void add(Donation x) {
        String sql = "INSERT INTO donations(donor_name, amount, type) VALUES(?, ?, ?)";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, x.getDonorName());
            ps.setDouble(2, x.getAmount());
            ps.setString(3, x.getType());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // READ
    public List<Donation> findAll() {
        List<Donation> list = new ArrayList<>();
        try (Statement st = conn().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM donations")) {
            while (rs.next()) {
                list.add(new Donation(rs.getInt("id"), rs.getString("donor_name"), rs.getDouble("amount"), rs.getString("type")));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    // UPDATE
    public void update(Donation x) {
        String sql = "UPDATE donations SET donor_name=?, amount=?, type=? WHERE id=?";
        try (PreparedStatement ps = conn().prepareStatement(sql)) {
            ps.setString(1, x.getDonorName());
            ps.setDouble(2, x.getAmount());
            ps.setString(3, x.getType());
            ps.setInt(4, x.getId());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // DELETE
    public void delete(int id) {
        try (PreparedStatement ps = conn().prepareStatement("DELETE FROM donations WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}
