package com.sanad.model.donation;

import java.math.BigDecimal;

/**
 * DECORATOR PATTERN (Abstract Decorator).
 * Holds a wrapped component and defines the base for optional donation add-ons.
 */
public abstract class DonationDecorator extends DonationComponent {

    protected final DonationComponent component;

    protected DonationDecorator(DonationComponent component) {
        if (component == null) {
            throw new IllegalArgumentException("Component to decorate cannot be null");
        }
        this.component = component;
    }

    @Override
    public abstract String getDescription();

    @Override
    public abstract BigDecimal getAmount();
}

