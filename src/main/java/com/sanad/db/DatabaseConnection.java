package com.sanad.db;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.StatementExceptions;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

/**
 * SINGLETON PATTERN.
 * Only one DatabaseConnection object exists for the whole application.
 * Everyone calls DatabaseConnection.getInstance() instead of "new".
 */
public class DatabaseConnection {

    private static final String CANNOT_CONNECT = "Cannot open database. Is MySQL running? (docker compose up -d)";

    private static DatabaseConnection instance;
    private final Jdbi jdbi;

    private DatabaseConnection() {
        Properties settings = new Properties();
        try (InputStream in = DatabaseConnection.class.getResourceAsStream("/db.properties")) {
            settings.load(in);
            String url = settings.getProperty("db.url");
            String user = settings.getProperty("db.user");
            String password = settings.getProperty("db.password");
            // Databases created before Flyway already match V2.
            Flyway.configure().dataSource(url, user, password)
                    .baselineOnMigrate(true).baselineVersion("2")
                    .load().migrate();
            jdbi = Jdbi.create(url, user, password);
            jdbi.installPlugin(new SqlObjectPlugin());
            jdbi.getConfig(StatementExceptions.class).setMessageRendering(StatementExceptions.MessageRendering.NONE);
        } catch (IOException e) {
            throw new RuntimeException(CANNOT_CONNECT, e);
        } catch (FlywayException e) {
            throw isConnectionFailure(e) ? new RuntimeException(CANNOT_CONNECT, e) : e;
        }
    }

    /**
     * True when MySQL could not be reached (SQLState class 08), as opposed to a failed migration.
     */
    private static boolean isConnectionFailure(Throwable e) {
        for (Throwable t = e; t != null; t = t.getCause()) {
            if (t instanceof SQLException sql && sql.getSQLState() != null && sql.getSQLState().startsWith("08")) {
                return true;
            }
        }
        return false;
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Entry point for the repositories; each call borrows its own connection.
     */
    public Jdbi getJdbi() {
        return jdbi;
    }
}
