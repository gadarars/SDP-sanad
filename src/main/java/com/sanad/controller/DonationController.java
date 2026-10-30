package com.sanad.controller;

import com.sanad.db.DatabaseConnection;
import com.sanad.model.donation.Donation;
import com.sanad.model.donation.DonationRepository;
import java.util.List;

/** CONTROLLER: sits between the view and the model. Put validation / business rules here. */
public class DonationController {

    public List<Donation> getAll() { return repo().findAll(); }

    public void add(String donorName, double amount, String type) {
        check(donorName, amount);
        repo().add(new Donation(0, donorName, amount, type));
    }

    public void update(int id, String donorName, double amount, String type) {
        check(donorName, amount);
        repo().update(new Donation(id, donorName, amount, type));
    }

    public void delete(int id) { repo().delete(id); }

    private void check(String donorName, double amount) {
        if (donorName == null || donorName.isBlank()) throw new IllegalArgumentException("Donor name is required");
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
    }

    private static DonationRepository repo() {
        return DatabaseConnection.getInstance().getJdbi().onDemand(DonationRepository.class);
    }
}
