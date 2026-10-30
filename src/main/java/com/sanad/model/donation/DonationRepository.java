package com.sanad.model.donation;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import java.util.List;

/** MODEL: database access (full CRUD) for donations. */
@RegisterConstructorMapper(Donation.class)
public interface DonationRepository {

    // CREATE
    @SqlUpdate("INSERT INTO donations(donor_name, amount, type) VALUES(:donorName, :amount, :type)")
    void add(@BindBean Donation x);

    // READ
    @SqlQuery("SELECT * FROM donations")
    List<Donation> findAll();

    // UPDATE
    @SqlUpdate("UPDATE donations SET donor_name=:donorName, amount=:amount, type=:type WHERE id=:id")
    void update(@BindBean Donation x);

    // DELETE
    @SqlUpdate("DELETE FROM donations WHERE id=:id")
    void delete(int id);
}
