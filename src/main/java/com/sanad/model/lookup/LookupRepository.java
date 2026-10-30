package com.sanad.model.lookup;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * MODEL: database access (full CRUD) for lookups.
 */
@RegisterConstructorMapper(Lookup.class)
public interface LookupRepository {

    // CREATE
    @SqlUpdate("INSERT INTO lookups(kind, name) VALUES(:kind, :name)")
    void add(@BindBean Lookup x);

    // READ
    @SqlQuery("SELECT * FROM lookups")
    List<Lookup> findAll();

    /**
     * Returns the lookups of one kind, e.g. the options for a dropdown.
     */
    @SqlQuery("SELECT * FROM lookups WHERE kind=:kind ORDER BY id")
    List<Lookup> findByKind(String kind);

    // UPDATE
    @SqlUpdate("UPDATE lookups SET kind=:kind, name=:name WHERE id=:id")
    void update(@BindBean Lookup x);

    // DELETE
    @SqlUpdate("DELETE FROM lookups WHERE id=:id")
    void delete(int id);
}
