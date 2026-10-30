package com.sanad.model.donation;

/**
 * MODEL: data object for one donation.
 */
public class Donation {
    private int id;
    private String donorName;
    private double amount;
    private String type;

    public Donation(int id, String donorName, double amount, String type) {
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
