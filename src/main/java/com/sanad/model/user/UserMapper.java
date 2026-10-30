package com.sanad.model.user;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Turns one database row into the right User subclass.
 * FACTORY METHOD PATTERN: the row's role picks the UserFactory subclass that builds it.
 */
public class UserMapper implements RowMapper<User> {
    @Override
    public User map(ResultSet rs, StatementContext ctx) throws SQLException {
        Map<String, String> details = new HashMap<>();
        for (String column : UserType.allFields()) details.put(column, rs.getString(column));
        UserFactory factory = UserType.valueOf(rs.getString("role")).getFactory();
        return factory.buildUser(rs.getInt("id"), rs.getString("username"), rs.getString("email"), details);
    }
}
