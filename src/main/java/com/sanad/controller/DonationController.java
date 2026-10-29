package com.sanad.controller;

import com.sanad.model.donation.Donation;
import com.sanad.model.donation.DonationRepository;
import java.util.List;

/** CONTROLLER: sits between the view and the model. Put validation / business rules here. */
public class DonationController {

    private final DonationRepository repo = new DonationRepository();

    public List<Donation> getAll() { return repo.findAll(); }

    public void add(String donorName, double amount, String type) {
        validate(donorName);
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        repo.add(new Donation(0, donorName, amount, type));
    }

    public void update(int id, String donorName, double amount, String type) {
        validate(donorName);
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        repo.update(new Donation(id, donorName, amount, type));
    }

    public void delete(int id) { repo.delete(id); }

    private void validate(String required) {
        if (required == null || required.isBlank())
            throw new IllegalArgumentException("Donor name is required");
    }
}
