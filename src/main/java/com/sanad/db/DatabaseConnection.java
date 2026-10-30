package com.sanad.db;

import com.sanad.model.lookup.Lookup;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * SINGLETON PATTERN.
 * Only one DatabaseConnection object exists for the whole application.
 * Everyone calls DatabaseConnection.getInstance() instead of "new".
 */
public class DatabaseConnection {

    private static final Map<String, List<String>> STARTING_LOOKUPS = Map.of(
            Lookup.EVENT_CATEGORY, List.of("FUNDRAISER", "WORKSHOP", "OUTREACH"),
            Lookup.NEED_TYPE, List.of("FOOD", "MEDICAL", "EDUCATION", "SHELTER"),
            Lookup.VOLUNTEER_SKILL, List.of("TEACHING", "FIRST_AID", "LOGISTICS", "FUNDRAISING"));

    private static DatabaseConnection instance;
    private final Properties settings = new Properties();
    private Connection connection;

    private DatabaseConnection() {
        try (InputStream in = DatabaseConnection.class.getResourceAsStream("/db.properties")) {
            settings.load(in);
            connection = open();
            createTables();
            seedLookups();
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Cannot open database. Is MySQL running? (docker compose up -d)", e);
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Returns the shared connection, reopening it if MySQL closed it (e.g. after a long idle).
     */
    public synchronized Connection getConnection() {
        try {
            if (!connection.isValid(2)) {
                connection = open();
            }
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Lost connection to MySQL", e);
        }
    }

    private Connection open() throws SQLException {
        return DriverManager.getConnection(settings.getProperty("db.url"),
                settings.getProperty("db.user"), settings.getProperty("db.password"));
    }

    /**
     * Creates every table the first time the app runs. Add new tables here.
     */
    private void createTables() throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.execute("""
                    CREATE TABLE IF NOT EXISTS donations (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        donor_name VARCHAR(255) NOT NULL,
                        amount DECIMAL(10,2) NOT NULL,
                        type VARCHAR(50) NOT NULL)""");
            st.execute("""
                    CREATE TABLE IF NOT EXISTS volunteers (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        skills VARCHAR(255),
                        availability VARCHAR(255),
                        hours DECIMAL(8,2) DEFAULT 0)""");
            st.execute("""
                    CREATE TABLE IF NOT EXISTS events (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        date VARCHAR(20),
                        location VARCHAR(255),
                        category VARCHAR(100))""");
            st.execute("""
                    CREATE TABLE IF NOT EXISTS beneficiaries (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        need VARCHAR(255),
                        contact VARCHAR(255))""");
            st.execute("""
                    CREATE TABLE IF NOT EXISTS lookups (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        kind VARCHAR(50) NOT NULL,
                        name VARCHAR(255) NOT NULL,
                        UNIQUE (kind, name))""");
            st.execute("""
                    CREATE TABLE IF NOT EXISTS users (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        username VARCHAR(100) NOT NULL,
                        email VARCHAR(255),
                        role VARCHAR(20) NOT NULL)""");
        }
    }

    /**
     * Fills in the starting values of each data-only list that has no values yet.
     */
    private void seedLookups() throws SQLException {
        try (PreparedStatement count = connection.prepareStatement("SELECT COUNT(*) FROM lookups WHERE kind=?");
             PreparedStatement insert = connection.prepareStatement("INSERT INTO lookups(kind, name) VALUES (?, ?)")) {
            for (Map.Entry<String, List<String>> list : STARTING_LOOKUPS.entrySet()) {
                count.setString(1, list.getKey());
                try (ResultSet rs = count.executeQuery()) {
                    rs.next();
                    if (rs.getInt(1) > 0) continue;
                }
                for (String name : list.getValue()) {
                    insert.setString(1, list.getKey());
                    insert.setString(2, name);
                    insert.executeUpdate();
                }
            }
        }
    }
}
