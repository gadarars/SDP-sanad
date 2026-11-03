package com.sanad.model.donation;

/**
 * DECORATOR PATTERN (Abstract Decorator): an add-on that wraps another donation component.
 */
public abstract class DonationDecorator extends DonationComponent {

    protected final DonationComponent component;

    protected DonationDecorator(DonationComponent component) {
        this.component = component;
    }

    @Override
    public abstract String getDescription();
}
