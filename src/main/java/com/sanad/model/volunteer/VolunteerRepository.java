package com.sanad.model.volunteer;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import java.util.List;

/** MODEL: database access (CRUD) for volunteers. */
@RegisterConstructorMapper(Volunteer.class)
public interface VolunteerRepository {

    @SqlUpdate("INSERT INTO volunteers(name, skills, availability, hours) VALUES(:name, :skills, :availability, :hours)")
    void add(@BindBean Volunteer v);

    @SqlQuery("SELECT * FROM volunteers ORDER BY id")
    List<Volunteer> findAll();

    @SqlUpdate("UPDATE volunteers SET name=:name, skills=:skills, availability=:availability, hours=:hours WHERE id=:id")
    void update(@BindBean Volunteer v);

    @SqlUpdate("DELETE FROM volunteers WHERE id=:id")
    void delete(int id);
}
