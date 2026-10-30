package com.sanad.controller;

import com.sanad.db.DatabaseConnection;
import com.sanad.model.beneficiary.Beneficiary;
import com.sanad.model.beneficiary.BeneficiaryRepository;
import com.sanad.model.beneficiary.Distribution;
import com.sanad.model.beneficiary.DistributionRepository;
import com.sanad.model.user.User;
import com.sanad.model.user.UserRepository;
import com.sanad.model.user.UserType;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.List;

/**
 * CONTROLLER: validates input from the view and calls the repository.
 */
public class BeneficiaryController {

    private static final String NEED_KIND = "NEED_TYPE";

    /**
     * Largest quantity the DECIMAL(10,2) column holds.
     */
    private static final BigDecimal MAX_QUANTITY = new BigDecimal("99999999.99");

    private final LookupController lookups = new LookupController();

    public List<Beneficiary> getAll() {
        return repo().findAll();
    }

    public List<String> getNeedTypes() {
        return lookups.getNames(NEED_KIND);
    }

    public List<User> getBeneficiaryUsers() {
        return userRepo().findAll().stream().filter(u -> u.getType() == UserType.BENEFICIARY).toList();
    }

    public void add(String name, String need, String contact, Integer userId) {
        Beneficiary b = validated(0, name, need, contact, userId);
        uniqueUser(() -> repo().add(b));
    }

    public void update(int id, String name, String need, String contact, Integer userId) {
        Beneficiary b = validated(id, name, need, contact, userId);
        uniqueUser(() -> repo().update(b));
    }

    public void delete(int id) {
        repo().delete(id);
    }

    public List<Distribution> getHistory(int beneficiaryId) {
        return distributions().findByBeneficiary(beneficiaryId);
    }

    public void addDistribution(int beneficiaryId, String item, BigDecimal quantity, LocalDate givenOn) {
        if (beneficiaryId <= 0) throw new IllegalArgumentException("Select a beneficiary first");
        distributions().add(validatedDistribution(0, beneficiaryId, item, quantity, givenOn));
    }

    public void updateDistribution(int id, String item, BigDecimal quantity, LocalDate givenOn) {
        distributions().update(validatedDistribution(id, 0, item, quantity, givenOn));
    }

    public void deleteDistribution(int id) {
        distributions().delete(id);
    }

    private Beneficiary validated(int id, String name, String need, String contact, Integer userId) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name is required");
        if (name.strip().length() > 255) throw new IllegalArgumentException("Name must be at most 255 characters");
        if (need == null || need.isBlank()) throw new IllegalArgumentException("Need type is required");
        if (!getNeedTypes().contains(need))
            throw new IllegalArgumentException("'" + need + "' is not in the " + NEED_KIND + " lookup list");
        String contactText = contact == null || contact.isBlank() ? null : contact.strip();
        if (contactText != null && contactText.length() > 255)
            throw new IllegalArgumentException("Contact must be at most 255 characters");
        if (userId != null) {
            boolean beneficiaryUser = userRepo().findById(userId).map(u -> u.getType() == UserType.BENEFICIARY).orElse(false);
            if (!beneficiaryUser) throw new IllegalArgumentException("The linked user must be a beneficiary user");
        }
        return new Beneficiary(id, name.strip(), need, contactText, userId);
    }

    private static void uniqueUser(Runnable write) {
        try {
            write.run();
        } catch (UnableToExecuteStatementException e) {
            if (!(e.getCause() instanceof SQLIntegrityConstraintViolationException)) throw e;
            throw new IllegalArgumentException("That user is already linked to another beneficiary");
        }
    }

    private static Distribution validatedDistribution(int id, int beneficiaryId, String item, BigDecimal quantity,
                                                      LocalDate givenOn) {
        if (item == null || item.isBlank()) throw new IllegalArgumentException("What was given is required");
        if (item.strip().length() > 255) throw new IllegalArgumentException("What was given must be at most 255 characters");
        if (quantity == null) throw new IllegalArgumentException("How much is required");
        if (quantity.signum() <= 0) throw new IllegalArgumentException("How much must be more than 0");
        if (quantity.stripTrailingZeros().scale() > 2)
            throw new IllegalArgumentException("How much can have at most 2 decimal places");
        if (quantity.compareTo(MAX_QUANTITY) > 0) throw new IllegalArgumentException("How much must be at most 99,999,999.99");
        if (givenOn == null) throw new IllegalArgumentException("Date is required");
        if (givenOn.isAfter(LocalDate.now())) throw new IllegalArgumentException("Date cannot be in the future");
        return new Distribution(id, beneficiaryId, item.strip(), quantity, givenOn);
    }

    private static BeneficiaryRepository repo() {
        return DatabaseConnection.getInstance().getJdbi().onDemand(BeneficiaryRepository.class);
    }

    private static DistributionRepository distributions() {
        return DatabaseConnection.getInstance().getJdbi().onDemand(DistributionRepository.class);
    }

    private static UserRepository userRepo() {
        return DatabaseConnection.getInstance().getJdbi().onDemand(UserRepository.class);
    }
}
