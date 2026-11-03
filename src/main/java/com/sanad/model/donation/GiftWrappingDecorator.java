package com.sanad.model.donation;

import java.math.BigDecimal;

/**
 * DECORATOR PATTERN (Concrete Decorator): adds gift wrapping to the donation.
 */
public class GiftWrappingDecorator extends DonationDecorator {

    public static final BigDecimal COST = new BigDecimal("25.00");

    public GiftWrappingDecorator(DonationComponent component) {
        super(component);
    }

    @Override
    public String getDescription() {
        return component.getDescription() + ", Gift wrapping";
    }

    @Override
    public BigDecimal cost() {
        return COST.add(component.cost());
    }
}
