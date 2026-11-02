package com.sanad.model.donation;

import java.math.BigDecimal;

/**
 * DECORATOR PATTERN (Concrete Decorator).
 * Adds a thank-you card add-on to the donation (+EGP 15.00).
 */
public class ThankYouCardDecorator extends DonationDecorator {

    private static final BigDecimal COST = new BigDecimal("15.00");

    public ThankYouCardDecorator(DonationComponent component) {
        super(component);
    }

    @Override
    public String getDescription() {
        return component.getDescription() + ", Thank-you card";
    }

    @Override
    public BigDecimal getAmount() {
        return component.getAmount().add(COST);
    }
}

