package com.sanad.model.donation;

import java.math.BigDecimal;

/**
 * DECORATOR PATTERN (Concrete Decorator).
 * Adds gift wrapping add-on to the donation (+EGP 25.00).
 */
public class GiftWrappingDecorator extends DonationDecorator {

    private static final BigDecimal COST = new BigDecimal("25.00");

    public GiftWrappingDecorator(DonationComponent component) {
        super(component);
    }

    @Override
    public String getDescription() {
        return component.getDescription() + ", Gift wrapping";
    }

    @Override
    public BigDecimal getAmount() {
        return component.getAmount().add(COST);
    }
}

