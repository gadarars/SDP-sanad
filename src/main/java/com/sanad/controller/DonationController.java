package com.sanad.controller;

import com.sanad.db.DatabaseConnection;
import com.sanad.model.donation.*;
import com.sanad.model.user.User;
import com.sanad.model.user.UserRepository;
import com.sanad.model.user.UserType;

import java.math.BigDecimal;
import java.util.List;

/** CONTROLLER: sits between the view and the model. Put validation / business rules here. */
public class DonationController {

    /** Largest amount the DECIMAL(10,2) column holds. */
    private static final BigDecimal MAX_AMOUNT = new BigDecimal("99999999.99");

    public List<Donation> getAll() {
        return repo().findAll();
    }

    /** All donations from the same donor: by linked user, else by donor name. */
    public List<Donation> getHistory(Donation d) {
        if (d.getUserId() != null) {
            return repo().findByUserId(d.getUserId());
        }
        return getAll().stream()
                .filter(x -> x.getUserId() == null && x.getDonorName().equalsIgnoreCase(d.getDonorName()))
                .toList();
    }

    /** Users with the DONOR role. */
    public List<User> getDonors() {
        return userRepo().findAll().stream()
                .filter(u -> u.getType() == UserType.DONOR)
                .toList();
    }

    public void add(Integer userId, String donorName, BigDecimal amount, DonationMethod method,
                    boolean giftWrapping, boolean thankYouCard) {
        check(donorName, amount, method);
        repo().add(new Donation(0, userId, donorName.strip(), amount, method.getName(), giftWrapping, thankYouCard));
    }

    public void update(int id, Integer userId, String donorName, BigDecimal amount, DonationMethod method,
                       boolean giftWrapping, boolean thankYouCard) {
        check(donorName, amount, method);
        repo().update(new Donation(id, userId, donorName.strip(), amount, method.getName(), giftWrapping, thankYouCard));
    }

    public void delete(int id) {
        repo().delete(id);
    }

    /** DECORATOR: wraps the base amount in the chosen add-ons. */
    public DonationComponent decorate(BigDecimal amount, boolean giftWrapping, boolean thankYouCard) {
        DonationComponent donation = new BaseDonationComponent(amount);
        if (giftWrapping) {
            donation = new GiftWrappingDecorator(donation);
        }
        if (thankYouCard) {
            donation = new ThankYouCardDecorator(donation);
        }
        return donation;
    }

    /** Printable receipt for a saved donation and its add-ons. */
    public String generateReceipt(Donation d) {
        DonationComponent total = decorate(d.getAmount(), d.isGiftWrapping(), d.isThankYouCard());

        StringBuilder sb = new StringBuilder();
        sb.append("══════════════════════════════════════════════════\n");
        sb.append("              SANAD CHARITY RECEIPT               \n");
        sb.append("══════════════════════════════════════════════════\n\n");
        sb.append(String.format("%-18s: #%d\n", "Receipt No", d.getId()));
        sb.append(String.format("%-18s: %s\n", "Donor Name", d.getDonorName()));
        sb.append(String.format("%-18s: %s\n", "Method", d.getDonationMethod().getLabel()));
        sb.append(String.format("%-18s: EGP %,.2f\n", "Base Amount", d.getAmount()));
        sb.append("──────────────────────────────────────────────────\n");
        sb.append(String.format("%-18s: %s\n", "Details", total.getDescription()));
        sb.append(String.format("%-18s: EGP %,.2f\n", "Total Amount", total.cost()));
        sb.append("──────────────────────────────────────────────────\n\n");
        sb.append("Status: ").append(d.performProcess()).append("\n\n");
        sb.append("         Thank you for your generosity!           \n");
        sb.append("══════════════════════════════════════════════════\n");
        return sb.toString();
    }

    private void check(String donorName, BigDecimal amount, DonationMethod method) {
        if (donorName == null || donorName.isBlank()) {
            throw new IllegalArgumentException("Donor is required");
        }
        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (amount.stripTrailingZeros().scale() > 2) {
            throw new IllegalArgumentException("Amount can have at most 2 decimal places");
        }
        if (amount.compareTo(MAX_AMOUNT) > 0) {
            throw new IllegalArgumentException("Amount must be at most 99,999,999.99");
        }
        if (method == null) {
            throw new IllegalArgumentException("Method is required");
        }
    }

    private static DonationRepository repo() {
        return DatabaseConnection.getInstance().getJdbi().onDemand(DonationRepository.class);
    }

    private static UserRepository userRepo() {
        return DatabaseConnection.getInstance().getJdbi().onDemand(UserRepository.class);
    }
}
