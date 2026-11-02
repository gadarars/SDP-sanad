package com.sanad.model.donation;

/**
 * STRATEGY PATTERN (Strategy interface).
 * Each donation method defines its own processing behaviour.
 * A donation holds one method and delegates method-specific work to it.
 */
public interface DonationMethod {

    /** The stored code, e.g. "ONLINE". */
    String getName();

    /** Human-readable label, e.g. "Online payment". */
    String getLabel();

    /**
     * Method-specific processing. Returns a confirmation message.
     * Each concrete strategy does its own work here.
     */
    String process(Donation donation);
}

