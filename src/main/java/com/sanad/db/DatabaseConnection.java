package com.sanad.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * SINGLETON PATTERN.
 * Only one DatabaseConnection object exists for the whole application.
 * Everyone calls DatabaseConnection.getInstance() instead of "new".
 */
public class DatabaseConnection {

    private static DatabaseConnection instance;   // the one and only instance
    private final Properties settings = new Properties();
    private Connection connection;

    private DatabaseConnection() {                // private: nobody else can call new
        try (InputStream in = DatabaseConnection.class.getResourceAsStream("/db.properties")) {
            settings.load(in);
            connection = open();
            createTables();
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

    /** Returns the shared connection, reopening it if MySQL closed it (e.g. after a long idle). */
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

    /** Creates every table the first time the app runs. Add new tables here. */
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
                    name VARCHAR(255))""");
            st.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(100) NOT NULL,
                    email VARCHAR(255),
                    role VARCHAR(20) NOT NULL)""");
        }
    }
}
