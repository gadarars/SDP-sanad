package com.sanad.model.user;

import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindMap;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * MODEL: database access (CRUD) for users, plus the lookups needed for login.
 * Rows are mapped to User subclasses by UserMapper (which uses UserFactory).
 */
@RegisterRowMapper(UserMapper.class)
public interface UserRepository {

    default void add(User u, String passwordHash) {
        insertRow(u, u.getDetails(), passwordHash);
    }

    /**
     * A null {@code passwordHash} keeps the current password.
     */
    default void update(User u, String passwordHash) {
        updateRow(u, u.getDetails(), passwordHash);
    }

    /**
     * Columns missing from {@code details} are stored as NULL.
     */
    @SqlUpdate("INSERT INTO users(username, email, role, password, job_title, phone, skills, address) "
            + "VALUES(:u.username, :u.email, :u.role, :password, :d.job_title, :d.phone, :d.skills, :d.address)")
    void insertRow(@BindBean("u") User u, @BindMap(value = "d", keys = {"job_title", "phone", "skills", "address"}) Map<String, String> details,
                   @Bind("password") String passwordHash);

    /**
     * Columns missing from {@code details} are set to NULL.
     */
    @SqlUpdate("UPDATE users SET username=:u.username, email=:u.email, role=:u.role, password=COALESCE(:password, password), "
            + "job_title=:d.job_title, phone=:d.phone, skills=:d.skills, address=:d.address WHERE id=:u.id")
    void updateRow(@BindBean("u") User u, @BindMap(value = "d", keys = {"job_title", "phone", "skills", "address"}) Map<String, String> details,
                   @Bind("password") String passwordHash);

    @SqlQuery("SELECT * FROM users ORDER BY id")
    List<User> findAll();

    @SqlQuery("SELECT * FROM users WHERE id = :id")
    Optional<User> findById(@Bind("id") int id);

    @SqlQuery("SELECT * FROM users WHERE username = :username")
    Optional<User> findByUsername(@Bind("username") String username);

    @SqlQuery("SELECT password FROM users WHERE username = :username")
    Optional<String> findPasswordHash(@Bind("username") String username);

    /**
     * How many users already have a password (0 on a brand-new or pre-login database).
     */
    @SqlQuery("SELECT COUNT(*) FROM users WHERE password IS NOT NULL")
    int countWithPassword();

    /**
     * Admins other than {@code id} who can log in.
     */
    @SqlQuery("SELECT COUNT(*) FROM users WHERE role = 'ADMIN' AND password IS NOT NULL AND id <> :id")
    int countOtherAdmins(@Bind("id") int id);

    @SqlUpdate("DELETE FROM users WHERE id=:id")
    void delete(int id);
}
