package com.sanad.model.beneficiary;

import com.sanad.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** MODEL: database access (CRUD) for beneficiaries. */
public class BeneficiaryRepository {

    public void add(Beneficiary b) {
        String sql = "INSERT INTO beneficiaries(name, need, contact) VALUES(?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setString(1, b.getName()); ps.setString(2, b.getNeed()); ps.setString(3, b.getContact());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<Beneficiary> findAll() {
        List<Beneficiary> list = new ArrayList<>();
        try (Statement st = DatabaseConnection.getInstance().getConnection().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM beneficiaries")) {
            while (rs.next()) {
                list.add(new Beneficiary(rs.getInt("id"), rs.getString("name"),
                        rs.getString("need"), rs.getString("contact")));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return list;
    }

    public void update(Beneficiary b) {
        String sql = "UPDATE beneficiaries SET name=?, need=?, contact=? WHERE id=?";
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setString(1, b.getName()); ps.setString(2, b.getNeed());
            ps.setString(3, b.getContact()); ps.setInt(4, b.getId());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void delete(int id) {
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement("DELETE FROM beneficiaries WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}
