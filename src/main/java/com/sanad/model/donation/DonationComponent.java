package com.sanad.model.donation;

import java.math.BigDecimal;

/**
 * DECORATOR PATTERN (Abstract Component): a donation with a description and a cost.
 */
public abstract class DonationComponent {

    protected String description = "Unknown donation";

    public String getDescription() {
        return description;
    }

    public abstract BigDecimal cost();
}
