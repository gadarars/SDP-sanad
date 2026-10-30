package com.sanad.model.event;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import java.util.List;

/** MODEL: database access (CRUD) for events. */
@RegisterConstructorMapper(Event.class)
public interface EventRepository {

    @SqlUpdate("INSERT INTO events(name, date, location, category) VALUES(:name, :date, :location, :category)")
    void add(@BindBean Event e);

    @SqlQuery("SELECT * FROM events")
    List<Event> findAll();

    @SqlUpdate("UPDATE events SET name=:name, date=:date, location=:location, category=:category WHERE id=:id")
    void update(@BindBean Event e);

    @SqlUpdate("DELETE FROM events WHERE id=:id")
    void delete(int id);
}
