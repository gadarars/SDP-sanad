package com.sanad.model.event;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import java.util.List;

/** MODEL: database access (CRUD) for event registrations. */
@RegisterConstructorMapper(Registration.class)
public interface RegistrationRepository {

    String SELECT = "SELECT r.id, r.event_id, e.name AS event_name, e.date AS event_date, r.user_id, u.username, r.attended "
            + "FROM event_registrations r JOIN events e ON e.id = r.event_id JOIN users u ON u.id = r.user_id ";

    @SqlUpdate("INSERT INTO event_registrations(event_id, user_id) VALUES(:eventId, :userId)")
    void add(@BindBean Registration r);

    @SqlQuery(SELECT + "ORDER BY e.date, e.name, u.username")
    List<Registration> findAll();

    @SqlQuery(SELECT + "WHERE r.event_id = :eventId ORDER BY u.username")
    List<Registration> findByEvent(int eventId);

    /** Attendance is cleared when the registration moves to another event or user. */
    @SqlUpdate("UPDATE event_registrations SET attended = (attended AND event_id = :eventId AND user_id = :userId), event_id=:eventId, user_id=:userId WHERE id=:id")
    void update(@BindBean Registration r);

    @SqlUpdate("UPDATE event_registrations SET attended = TRUE WHERE id=:id")
    void markAttended(int id);

    @SqlUpdate("DELETE FROM event_registrations WHERE id=:id")
    void delete(int id);
}
