package com.sanad.model.user;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import java.util.List;

/** MODEL: database access (CRUD) for users. */
@RegisterConstructorMapper(User.class)
public interface UserRepository {

    @SqlUpdate("INSERT INTO users(username, email, role) VALUES(:username, :email, :role)")
    void add(@BindBean User u);

    @SqlQuery("SELECT * FROM users ORDER BY id")
    List<User> findAll();

    @SqlUpdate("UPDATE users SET username=:username, email=:email, role=:role WHERE id=:id")
    void update(@BindBean User u);

    @SqlUpdate("DELETE FROM users WHERE id=:id")
    void delete(int id);
}
