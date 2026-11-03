package com.sanad.model.donation;

/**
 * STRATEGY PATTERN (Concrete Strategy): donation by cheque.
 */
public class CheckMethod implements DonationMethod {

    @Override
    public String getName() {
        return "CHECK";
    }

    @Override
    public String getLabel() {
        return "Cheque";
    }

    @Override
    public String process(Donation donation) {
        return "Cheque for EGP " + donation.getAmount()
                + " recorded — pending clearance";
    }
}

