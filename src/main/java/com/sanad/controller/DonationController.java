package com.sanad.controller;

import com.sanad.db.DatabaseConnection;
import com.sanad.model.donation.*;
import com.sanad.model.user.User;
import com.sanad.model.user.UserRepository;
import com.sanad.model.user.UserType;

import java.math.BigDecimal;
import java.util.List;

/**
 * CONTROLLER: sits between the view and the model.
 * Handles validation, business rules, donor history, receipts, and pattern integration.
 */
public class DonationController {

    /** Largest amount the DECIMAL(10,2) column holds. */
    private static final BigDecimal MAX_AMOUNT = new BigDecimal("99999999.99");

    public List<Donation> getAll() {
        return repo().findAll();
    }

    /**
     * Returns all donations for one donor user.
     */
    public List<Donation> getByDonor(int userId) {
        return repo().findByUserId(userId);
    }

    /**
     * Returns donation history for a donation record (by userId or donorName).
     */
    public List<Donation> getHistory(Donation d) {
        if (d == null) {
            return List.of();
        }
        if (d.getUserId() != null && d.getUserId() > 0) {
            return getByDonor(d.getUserId());
        }
        return getAll().stream()
                .filter(x -> x.getDonorName() != null && x.getDonorName().equalsIgnoreCase(d.getDonorName()))
                .toList();
    }

    /**
     * Returns all registered users with the DONOR role.
     */
    public List<User> getDonors() {
        return userRepo().findAll().stream()
                .filter(u -> u.getType() == UserType.DONOR)
                .toList();
    }

    /**
     * Adds a donation and executes the selected strategy's method-specific work.
     * Returns the strategy confirmation message.
     */
    public String add(Integer userId, String donorName, BigDecimal amount, DonationMethod method) {
        check(donorName, amount, method);
        Donation d = new Donation(0, userId, donorName.strip(), amount, method.getName());
        d.setDonationMethod(method);
        repo().add(d);
        return d.processMethod();
    }

    /**
     * Updates a donation and executes the selected strategy's method-specific work.
     * Returns the strategy confirmation message.
     */
    public String update(int id, Integer userId, String donorName, BigDecimal amount, DonationMethod method) {
        check(donorName, amount, method);
        Donation d = new Donation(id, userId, donorName.strip(), amount, method.getName());
        d.setDonationMethod(method);
        repo().update(d);
        return d.processMethod();
    }

    public void delete(int id) {
        repo().delete(id);
    }

    /**
     * DECORATOR: Builds a decorated donation component with the chosen add-ons.
     */
    public DonationComponent decorate(BigDecimal baseAmount, boolean giftWrapping, boolean thankYouCard) {
        DonationComponent component = new BaseDonationComponent("Donation", baseAmount != null ? baseAmount : BigDecimal.ZERO);
        if (giftWrapping) {
            component = new GiftWrappingDecorator(component);
        }
        if (thankYouCard) {
            component = new ThankYouCardDecorator(component);
        }
        return component;
    }

    /**
     * Generates a printable receipt string for a donation, optionally including add-on extras.
     */
    public String generateReceipt(Donation d, boolean giftWrapping, boolean thankYouCard) {
        if (d == null) {
            throw new IllegalArgumentException("No donation selected");
        }
        DonationComponent comp = decorate(d.getAmount(), giftWrapping, thankYouCard);

        StringBuilder sb = new StringBuilder();
        sb.append("══════════════════════════════════════════════════\n");
        sb.append("              SANAD CHARITY RECEIPT               \n");
        sb.append("══════════════════════════════════════════════════\n\n");
        sb.append(String.format("%-18s: #%d\n", "Receipt No", d.getId()));
        sb.append(String.format("%-18s: %s\n", "Donor Name", d.getDonorName()));
        sb.append(String.format("%-18s: %s\n", "Method", d.getDonationMethod().getLabel()));
        sb.append(String.format("%-18s: EGP %,.2f\n", "Base Amount", d.getAmount()));
        if (giftWrapping) {
            sb.append(String.format("%-18s: EGP %,.2f\n", "  + Gift Wrapping", new BigDecimal("25.00")));
        }
        if (thankYouCard) {
            sb.append(String.format("%-18s: EGP %,.2f\n", "  + Thank-You Card", new BigDecimal("15.00")));
        }
        sb.append("──────────────────────────────────────────────────\n");
        sb.append(String.format("%-18s: %s\n", "Details", comp.getDescription()));
        sb.append(String.format("%-18s: EGP %,.2f\n", "Total Amount", comp.getAmount()));
        sb.append("──────────────────────────────────────────────────\n\n");
        sb.append("Status: ").append(d.processMethod()).append("\n\n");
        sb.append("         Thank you for your generosity!           \n");
        sb.append("══════════════════════════════════════════════════\n");
        return sb.toString();
    }

    private void check(String donorName, BigDecimal amount, DonationMethod method) {
        if (donorName == null || donorName.isBlank()) {
            throw new IllegalArgumentException("Donor is required");
        }
        if (amount == null || amount.signum() <= 0) {
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
