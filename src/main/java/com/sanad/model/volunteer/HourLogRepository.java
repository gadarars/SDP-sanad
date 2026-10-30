package com.sanad.model.volunteer;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import java.util.List;

/** MODEL: database access (CRUD) for hour logs. */
@RegisterConstructorMapper(HourLog.class)
public interface HourLogRepository {

    @SqlUpdate("INSERT INTO volunteer_hours(assignment_id, hours) VALUES(:assignmentId, :hours)")
    void add(@BindBean HourLog h);

    @SqlQuery("SELECT h.id, h.assignment_id, v.name AS volunteer_name, e.name AS event_name, e.date AS event_date, h.hours "
            + "FROM volunteer_hours h JOIN volunteer_assignments a ON a.id = h.assignment_id "
            + "JOIN volunteers v ON v.id = a.volunteer_id JOIN events e ON e.id = a.event_id ORDER BY h.id")
    List<HourLog> findAll();

    @SqlUpdate("UPDATE volunteer_hours SET assignment_id=:assignmentId, hours=:hours WHERE id=:id")
    void update(@BindBean HourLog h);

    @SqlUpdate("DELETE FROM volunteer_hours WHERE id=:id")
    void delete(int id);
}
