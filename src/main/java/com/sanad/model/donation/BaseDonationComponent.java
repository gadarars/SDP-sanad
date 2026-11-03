package com.sanad.model.donation;

import java.math.BigDecimal;

/**
 * DECORATOR PATTERN (Concrete Component): the donation amount before any add-ons.
 */
public class BaseDonationComponent extends DonationComponent {

    private final BigDecimal amount;

    public BaseDonationComponent(BigDecimal amount) {
        description = "Donation";
        this.amount = amount;
    }

    @Override
    public BigDecimal cost() {
        return amount;
    }
}
