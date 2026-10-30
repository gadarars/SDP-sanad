package com.sanad.model.user;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

/**
 * MODEL: database access (CRUD) for users, plus the lookups needed for login.
 * Rows are mapped to User subclasses by UserMapper (which uses UserFactory).
 */
@RegisterRowMapper(UserMapper.class)
public interface UserRepository {

    @SqlUpdate("INSERT INTO users(username, email, role, password) VALUES(:u.username, :u.email, :u.role, :password)")
    void add(@BindBean("u") User u, @Bind("password") String passwordHash);

    @SqlQuery("SELECT * FROM users ORDER BY id")
    List<User> findAll();

    @SqlQuery("SELECT * FROM users WHERE username = :username")
    Optional<User> findByUsername(@Bind("username") String username);

    @SqlQuery("SELECT password FROM users WHERE username = :username")
    Optional<String> findPasswordHash(@Bind("username") String username);

    /**
     * How many users already have a password (0 on a brand-new or pre-login database).
     */
    @SqlQuery("SELECT COUNT(*) FROM users WHERE password IS NOT NULL")
    int countWithPassword();

    @SqlUpdate("UPDATE users SET username=:u.username, email=:u.email, role=:u.role WHERE id=:u.id")
    void update(@BindBean("u") User u);

    @SqlUpdate("UPDATE users SET username=:u.username, email=:u.email, role=:u.role, password=:password WHERE id=:u.id")
    void updateWithPassword(@BindBean("u") User u, @Bind("password") String passwordHash);

    @SqlUpdate("DELETE FROM users WHERE id=:id")
    void delete(int id);
}
