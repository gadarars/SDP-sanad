package com.sanad.model.donation;

import java.math.BigDecimal;

/**
 * MODEL: data object for one donation.
 * STRATEGY PATTERN (Context): holds a DonationMethod and delegates method-specific work to it.
 */
public class Donation {
    private int id;
    private Integer userId;
    private String donorName;
    private BigDecimal amount;
    private DonationMethod donationMethod;
    private boolean giftWrapping;
    private boolean thankYouCard;

    public Donation(int id, Integer userId, String donorName, BigDecimal amount, String method,
                    boolean giftWrapping, boolean thankYouCard) {
        this.id = id;
        this.userId = userId;
        this.donorName = donorName;
        this.amount = amount;
        this.donationMethod = DonationMethods.byName(method);
        this.giftWrapping = giftWrapping;
        this.thankYouCard = thankYouCard;
    }

    public int getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /** Stored code of the donation method, e.g. "ONLINE". */
    public String getMethod() {
        return donationMethod.getName();
    }

    public DonationMethod getDonationMethod() {
        return donationMethod;
    }

    public void setDonationMethod(DonationMethod donationMethod) {
        this.donationMethod = donationMethod;
    }

    public boolean isGiftWrapping() {
        return giftWrapping;
    }

    public boolean isThankYouCard() {
        return thankYouCard;
    }

    public String performProcess() {
        return donationMethod.process(this);
    }
}
