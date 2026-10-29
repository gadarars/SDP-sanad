package com.sanad.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * SINGLETON PATTERN.
 * Only one DatabaseConnection object exists for the whole application.
 * Everyone calls DatabaseConnection.getInstance() instead of "new".
 */
public class DatabaseConnection {

    private static DatabaseConnection instance;   // the one and only instance
    private final Connection connection;

    private DatabaseConnection() {                // private: nobody else can call new
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:sanad.db");
            createTables();
        } catch (SQLException e) {
            throw new RuntimeException("Cannot open database", e);
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    /** Creates every table the first time the app runs. Add new tables here. */
    private void createTables() throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.execute("""
                CREATE TABLE IF NOT EXISTS donations (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    donor_name TEXT NOT NULL,
                    amount REAL NOT NULL,
                    type TEXT NOT NULL)""");
            st.execute("""
                CREATE TABLE IF NOT EXISTS volunteers (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    skills TEXT,
                    availability TEXT,
                    hours REAL DEFAULT 0)""");
            st.execute("""
                CREATE TABLE IF NOT EXISTS events (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    date TEXT,
                    location TEXT,
                    category TEXT)""");
            st.execute("""
                CREATE TABLE IF NOT EXISTS beneficiaries (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    need TEXT,
                    contact TEXT)""");
            st.execute("""
                CREATE TABLE IF NOT EXISTS lookups (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    kind TEXT NOT NULL,
                    name TEXT)""");
            st.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL,
                    email TEXT,
                    role TEXT NOT NULL)""");
        }
    }
}
