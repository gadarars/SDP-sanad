package com.sanad.controller;

import com.sanad.db.DatabaseConnection;
import com.sanad.model.beneficiary.Beneficiary;
import com.sanad.model.beneficiary.BeneficiaryRepository;

import java.util.List;

/**
 * CONTROLLER: validates input from the view and calls the repository.
 */
public class BeneficiaryController {

    public List<Beneficiary> getAll() {
        return repo().findAll();
    }

    public void add(String name, String need, String contact) {
        check(name);
        repo().add(new Beneficiary(0, name, need, contact));
    }

    public void update(int id, String name, String need, String contact) {
        check(name);
        repo().update(new Beneficiary(id, name, need, contact));
    }

    public void delete(int id) {
        repo().delete(id);
    }

    private void check(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name is required");
    }

    private static BeneficiaryRepository repo() {
        return DatabaseConnection.getInstance().getJdbi().onDemand(BeneficiaryRepository.class);
    }
}
