package com.sanad.model.volunteer;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import java.util.List;
import java.util.Optional;

/** MODEL: database access (CRUD) for volunteer assignments. */
@RegisterConstructorMapper(Assignment.class)
public interface AssignmentRepository {

    String SELECT = "SELECT a.id, a.volunteer_id, v.name AS volunteer_name, a.event_id, e.name AS event_name, "
            + "e.date AS event_date, a.skill, "
            + "(SELECT COALESCE(SUM(h.hours), 0) FROM volunteer_hours h WHERE h.assignment_id = a.id) AS hours "
            + "FROM volunteer_assignments a JOIN volunteers v ON v.id = a.volunteer_id JOIN events e ON e.id = a.event_id ";

    @SqlUpdate("INSERT INTO volunteer_assignments(volunteer_id, event_id, skill) VALUES(:volunteerId, :eventId, :skill)")
    void add(@BindBean Assignment a);

    @SqlQuery(SELECT + "ORDER BY e.date DESC, a.id")
    List<Assignment> findAll();

    @SqlQuery(SELECT + "WHERE a.id = :id")
    Optional<Assignment> findById(int id);

    @SqlQuery(SELECT + "WHERE a.volunteer_id = :volunteerId ORDER BY e.date, a.id")
    List<Assignment> findByVolunteer(int volunteerId);

    @SqlUpdate("UPDATE volunteer_assignments SET volunteer_id=:volunteerId, event_id=:eventId, skill=:skill WHERE id=:id")
    void update(@BindBean Assignment a);

    @SqlUpdate("DELETE FROM volunteer_assignments WHERE id=:id")
    void delete(int id);
}
