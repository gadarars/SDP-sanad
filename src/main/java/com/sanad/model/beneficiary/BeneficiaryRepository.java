package com.sanad.model.beneficiary;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * MODEL: database access (CRUD) for beneficiaries.
 */
@RegisterConstructorMapper(Beneficiary.class)
public interface BeneficiaryRepository {

    @SqlUpdate("INSERT INTO beneficiaries(name, need, contact, user_id) VALUES(:name, :need, :contact, :userId)")
    void add(@BindBean Beneficiary b);

    @SqlQuery("SELECT * FROM beneficiaries ORDER BY id")
    List<Beneficiary> findAll();

    @SqlUpdate("UPDATE beneficiaries SET name=:name, need=:need, contact=:contact, user_id=:userId WHERE id=:id")
    void update(@BindBean Beneficiary b);

    @SqlUpdate("DELETE FROM beneficiaries WHERE id=:id")
    void delete(int id);
}
