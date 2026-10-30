package com.sanad.controller;

import com.sanad.db.DatabaseConnection;
import com.sanad.model.donation.Donation;
import com.sanad.model.donation.DonationRepository;
import java.math.BigDecimal;
import java.util.List;

/** CONTROLLER: sits between the view and the model. Put validation / business rules here. */
public class DonationController {

    /** Largest amount the DECIMAL(10,2) column holds. */
    private static final BigDecimal MAX_AMOUNT = new BigDecimal("99999999.99");

    public List<Donation> getAll() { return repo().findAll(); }

    public void add(String donorName, BigDecimal amount, String type) {
        check(donorName, amount);
        repo().add(new Donation(0, donorName, amount, type));
    }

    public void update(int id, String donorName, BigDecimal amount, String type) {
        check(donorName, amount);
        repo().update(new Donation(id, donorName, amount, type));
    }

    public void delete(int id) { repo().delete(id); }

    private void check(String donorName, BigDecimal amount) {
        if (donorName == null || donorName.isBlank()) throw new IllegalArgumentException("Donor name is required");
        if (amount.signum() <= 0) throw new IllegalArgumentException("Amount must be positive");
        if (amount.stripTrailingZeros().scale() > 2) throw new IllegalArgumentException("Amount can have at most 2 decimal places");
        if (amount.compareTo(MAX_AMOUNT) > 0) throw new IllegalArgumentException("Amount must be at most 99,999,999.99");
    }

    private static DonationRepository repo() {
        return DatabaseConnection.getInstance().getJdbi().onDemand(DonationRepository.class);
    }
}
