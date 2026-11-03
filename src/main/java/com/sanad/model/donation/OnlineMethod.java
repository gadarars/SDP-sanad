package com.sanad.model.donation;

/**
 * STRATEGY PATTERN (Concrete Strategy): online donation via payment gateway.
 */
public class OnlineMethod implements DonationMethod {

    @Override
    public String getName() {
        return "ONLINE";
    }

    @Override
    public String getLabel() {
        return "Online payment";
    }

    @Override
    public String process(Donation donation) {
        return "Online payment of EGP " + donation.getAmount()
                + " processed via payment gateway";
    }
}

