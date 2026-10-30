package com.sanad.model.volunteer;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import java.util.List;
import java.util.Optional;

/** MODEL: database access (CRUD) for volunteers. */
@RegisterConstructorMapper(Volunteer.class)
public interface VolunteerRepository {

    String SELECT = "SELECT v.id, v.user_id, u.username, v.name, v.skills, v.available_days, "
            + "(SELECT COALESCE(SUM(h.hours), 0) FROM volunteer_hours h "
            + "JOIN volunteer_assignments a ON a.id = h.assignment_id WHERE a.volunteer_id = v.id) AS total_hours "
            + "FROM volunteers v LEFT JOIN users u ON u.id = v.user_id ";

    @SqlUpdate("INSERT INTO volunteers(user_id, name, skills, available_days) VALUES(:userId, :name, :skills, :availableDays)")
    void add(@BindBean Volunteer v);

    @SqlQuery(SELECT + "ORDER BY v.id")
    List<Volunteer> findAll();

    @SqlQuery(SELECT + "WHERE v.id = :id")
    Optional<Volunteer> findById(int id);

    @SqlUpdate("UPDATE volunteers SET user_id=:userId, name=:name, skills=:skills, available_days=:availableDays WHERE id=:id")
    void update(@BindBean Volunteer v);

    @SqlUpdate("DELETE FROM volunteers WHERE id=:id")
    void delete(int id);
}
