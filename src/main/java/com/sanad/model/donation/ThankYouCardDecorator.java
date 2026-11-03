package com.sanad.model.donation;

import java.math.BigDecimal;

/**
 * DECORATOR PATTERN (Concrete Decorator): adds a thank-you card to the donation.
 */
public class ThankYouCardDecorator extends DonationDecorator {

    public static final BigDecimal COST = new BigDecimal("15.00");

    public ThankYouCardDecorator(DonationComponent component) {
        super(component);
    }

    @Override
    public String getDescription() {
        return component.getDescription() + ", Thank-you card";
    }

    @Override
    public BigDecimal cost() {
        return COST.add(component.cost());
    }
}
