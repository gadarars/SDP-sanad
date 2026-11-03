package com.sanad.model.donation;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

/** MODEL: database access (full CRUD) for donations. */
@RegisterConstructorMapper(Donation.class)
public interface DonationRepository {

    String SELECT = "SELECT d.id, d.user_id, COALESCE(u.username, d.donor_name) AS donor_name, d.amount, d.method, "
            + "d.add_ons FROM donations d LEFT JOIN users u ON d.user_id = u.id ";

    // CREATE
    @SqlUpdate("INSERT INTO donations(user_id, donor_name, amount, method, add_ons) "
            + "VALUES(:userId, :donorName, :amount, :method, :addOnCodes)")
    void add(@BindBean Donation x);

    // READ
    @SqlQuery(SELECT + "ORDER BY d.id")
    List<Donation> findAll();

    @SqlQuery(SELECT + "WHERE d.id = :id")
    Optional<Donation> findById(@Bind("id") int id);

    // READ BY DONOR
    @SqlQuery(SELECT + "WHERE d.user_id = :userId ORDER BY d.id")
    List<Donation> findByUserId(@Bind("userId") int userId);

    // UPDATE
    @SqlUpdate("UPDATE donations SET user_id=:userId, donor_name=:donorName, amount=:amount, method=:method, "
            + "add_ons=:addOnCodes WHERE id=:id")
    void update(@BindBean Donation x);

    // DELETE
    @SqlUpdate("DELETE FROM donations WHERE id=:id")
    void delete(@Bind("id") int id);
}
