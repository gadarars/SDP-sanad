package com.sanad.model.beneficiary;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * MODEL: database access (CRUD) for distributions.
 */
@RegisterConstructorMapper(Distribution.class)
public interface DistributionRepository {

    @SqlUpdate("INSERT INTO distributions(beneficiary_id, item, quantity, given_on) VALUES(:beneficiaryId, :item, :quantity, :givenOn)")
    void add(@BindBean Distribution d);

    @SqlQuery("SELECT * FROM distributions WHERE beneficiary_id = :beneficiaryId ORDER BY given_on DESC, id DESC")
    List<Distribution> findByBeneficiary(@Bind("beneficiaryId") int beneficiaryId);

    @SqlUpdate("UPDATE distributions SET item=:item, quantity=:quantity, given_on=:givenOn WHERE id=:id")
    void update(@BindBean Distribution d);

    @SqlUpdate("DELETE FROM distributions WHERE id=:id")
    void delete(int id);
}
