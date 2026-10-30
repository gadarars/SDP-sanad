package com.sanad.model.user;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Turns one database row into the right User subclass.
 * FACTORY PATTERN: the row's role is handed to UserFactory, which decides the class.
 */
public class UserMapper implements RowMapper<User> {
    @Override
    public User map(ResultSet rs, StatementContext ctx) throws SQLException {
        return UserFactory.create(rs.getInt("id"), rs.getString("role"),
                rs.getString("username"), rs.getString("email"));
    }
}
