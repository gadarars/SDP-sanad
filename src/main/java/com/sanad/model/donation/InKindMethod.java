package com.sanad.model.donation;

/**
 * STRATEGY PATTERN (Concrete Strategy): in-kind donation (goods or services).
 */
public class InKindMethod implements DonationMethod {

    @Override
    public String getName() {
        return "IN_KIND";
    }

    @Override
    public String getLabel() {
        return "In-kind (goods/services)";
    }

    @Override
    public String process(Donation donation) {
        return "In-kind donation valued at EGP " + donation.getAmount()
                + " — items catalogued for distribution";
    }
}

