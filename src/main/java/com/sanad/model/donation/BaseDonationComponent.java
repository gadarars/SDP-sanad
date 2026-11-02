package com.sanad.model.donation;

import java.math.BigDecimal;

/**
 * DECORATOR PATTERN (Concrete Component).
 * The base donation before any extras are applied.
 */
public class BaseDonationComponent extends DonationComponent {

    private final BigDecimal amount;

    public BaseDonationComponent(BigDecimal amount) {
        this("Donation", amount);
    }

    public BaseDonationComponent(String description, BigDecimal amount) {
        this.description = description != null ? description : "Donation";
        this.amount = amount != null ? amount : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getAmount() {
        return amount;
    }
}

