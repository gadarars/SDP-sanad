package com.sanad.controller;

import com.sanad.auth.Session;
import com.sanad.db.DatabaseConnection;
import com.sanad.model.donation.*;
import com.sanad.model.user.User;
import com.sanad.model.user.UserRepository;
import com.sanad.model.user.UserType;

import java.math.BigDecimal;
import java.util.List;

/** CONTROLLER: validates input from the view and calls the repository. */
public class DonationController {

    /** Largest amount the DECIMAL(10,2) column holds. */
    private static final BigDecimal MAX_AMOUNT = new BigDecimal("99999999.99");

    /** Staff see every donation; a donor sees only their own. */
    public List<Donation> getAll() {
        if (Session.getInstance().managesAllRecords()) return repo().findAll();
        return repo().findByUserId(Session.getInstance().getCurrentUser().getId());
    }

    /** All donations from the same donor: by linked user, else by donor name. */
    public List<Donation> getHistory(Donation d) {
        if (d.getUserId() != null) return repo().findByUserId(d.getUserId());
        return getAll().stream()
                .filter(x -> x.getUserId() == null && x.getDonorName().equalsIgnoreCase(d.getDonorName()))
                .toList();
    }

    /** Total given across {@code donations}, add-ons included. */
    public BigDecimal total(List<Donation> donations) {
        return donations.stream().map(d -> decorate(d).cost()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** Users with the DONOR role; a donor can only pick themselves. */
    public List<User> getDonors() {
        User me = Session.getInstance().getCurrentUser();
        if (!me.managesAllRecords()) return List.of(me);
        return userRepo().findAll().stream()
                .filter(u -> u.getType() == UserType.DONOR)
                .toList();
    }

    public boolean choosesDonor() {
        return Session.getInstance().managesAllRecords();
    }

    public void add(Integer userId, String donorName, BigDecimal amount, DonationMethod method, List<AddOn> addOns) {
        check(donorName, amount, method);
        Donation d = new Donation(0, userId, donorName.strip(), amount, method, addOns);
        ownedByCurrentDonor(d);
        repo().add(d);
    }

    /** STRATEGY: the stored donation switches to the chosen method at runtime. */
    public void update(int id, Integer userId, String donorName, BigDecimal amount, DonationMethod method,
                       List<AddOn> addOns) {
        check(donorName, amount, method);
        Donation d = find(id);
        d.setUserId(userId);
        d.setDonorName(donorName.strip());
        d.setAmount(amount);
        d.setDonationMethod(method);
        d.setAddOns(addOns);
        ownedByCurrentDonor(d);
        repo().update(d);
    }

    public void delete(int id) {
        find(id);
        repo().delete(id);
    }

    /** DECORATOR: wraps the donation in each chosen add-on, in order. */
    public DonationComponent decorate(Donation d) {
        DonationComponent donation = d;
        for (AddOn addOn : d.getAddOns()) {
            donation = addOn.wrap(donation);
        }
        return donation;
    }

    /** The decorated total of a donation that is not saved yet. */
    public DonationComponent preview(BigDecimal amount, List<AddOn> addOns) {
        return decorate(new Donation(0, null, "", amount, null, addOns));
    }

    /** Printable receipt for a saved donation and its add-ons. */
    public String generateReceipt(Donation d) {
        DonationComponent total = decorate(d);

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

    /** A donor's donations are always linked to their own account. */
    private static void ownedByCurrentDonor(Donation d) {
        User me = Session.getInstance().getCurrentUser();
        if (me.managesAllRecords()) return;
        d.setUserId(me.getId());
        d.setDonorName(me.getUsername());
    }

    private static Donation find(int id) {
        Donation d = repo().findById(id).orElseThrow(() -> new IllegalArgumentException("Donation not found"));
        Session.getInstance().requireOwner(d.getUserId());
        return d;
    }

    private void check(String donorName, BigDecimal amount, DonationMethod method) {
        if (donorName == null || donorName.isBlank()) throw new IllegalArgumentException("Donor is required");
        if (amount.signum() <= 0) throw new IllegalArgumentException("Amount must be positive");
        if (amount.stripTrailingZeros().scale() > 2)
            throw new IllegalArgumentException("Amount can have at most 2 decimal places");
        if (amount.compareTo(MAX_AMOUNT) > 0) throw new IllegalArgumentException("Amount must be at most 99,999,999.99");
        if (method == null) throw new IllegalArgumentException("Method is required");
    }

    private static DonationRepository repo() {
        return DatabaseConnection.getInstance().getJdbi().onDemand(DonationRepository.class);
    }

    private static UserRepository userRepo() {
        return DatabaseConnection.getInstance().getJdbi().onDemand(UserRepository.class);
    }
}
