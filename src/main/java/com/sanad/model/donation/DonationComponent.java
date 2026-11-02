package com.sanad.model.donation;

import java.math.BigDecimal;

/**
 * DECORATOR PATTERN (Abstract Component).
 * Exposes a description and an amount that decorators can modify.
 */
public abstract class DonationComponent {

    protected String description = "Donation";

    public String getDescription() {
        return description;
    }

    public abstract BigDecimal getAmount();
}

