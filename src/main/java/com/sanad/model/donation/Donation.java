package com.sanad.model.donation;

import java.math.BigDecimal;

/**
 * MODEL: data object for one donation.
 */
public class Donation {
    private int id;
    private String donorName;
    private BigDecimal amount;
    private String type;

    public Donation(int id, String donorName, BigDecimal amount, String type) {
        this.id = id;
        this.donorName = donorName;
        this.amount = amount;
        this.type = type;
    }

    public int getId() {
        return id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
