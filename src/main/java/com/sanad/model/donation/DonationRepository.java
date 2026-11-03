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

    @SqlUpdate("INSERT INTO donations(user_id, donor_name, amount, method, add_ons) "
            + "VALUES(:userId, :donorName, :amount, :method, :addOnCodes)")
    void add(@BindBean Donation x);

    @SqlQuery(SELECT + "ORDER BY d.id")
    List<Donation> findAll();

    @SqlQuery(SELECT + "WHERE d.id = :id")
    Optional<Donation> findById(@Bind("id") int id);

    @SqlQuery(SELECT + "WHERE d.user_id = :userId ORDER BY d.id")
    List<Donation> findByUserId(@Bind("userId") int userId);

    @SqlUpdate("UPDATE donations SET user_id=:userId, donor_name=:donorName, amount=:amount, method=:method, "
            + "add_ons=:addOnCodes WHERE id=:id")
    void update(@BindBean Donation x);

    @SqlUpdate("DELETE FROM donations WHERE id=:id")
    void delete(@Bind("id") int id);
}
