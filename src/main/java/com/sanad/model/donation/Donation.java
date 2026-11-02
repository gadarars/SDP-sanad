package com.sanad.model.donation;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.math.BigDecimal;

/**
 * MODEL: data object for one donation.
 * Holds a DonationMethod (Strategy) and delegates method-specific work to it.
 */
public class Donation {
    private int id;
    private Integer userId;
    private String donorName;
    private BigDecimal amount;
    private String method;
    private DonationMethod donationMethod;

    public Donation(int id,
                    @ColumnName("user_id") Integer userId,
                    @ColumnName("donor_name") String donorName,
                    BigDecimal amount,
                    String method) {
        this.id = id;
        this.userId = userId;
        this.donorName = donorName;
        this.amount = amount;
        this.method = method;
        this.donationMethod = DonationMethods.byName(method);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
        this.donationMethod = DonationMethods.byName(method);
    }

    /**
     * STRATEGY: Sets the donation method at runtime.
     */
    public void setDonationMethod(DonationMethod donationMethod) {
        if (donationMethod != null) {
            this.donationMethod = donationMethod;
            this.method = donationMethod.getName();
        }
    }

    public DonationMethod getDonationMethod() {
        if (donationMethod == null && method != null) {
            donationMethod = DonationMethods.byName(method);
        }
        return donationMethod;
    }

    /**
     * STRATEGY: Delegates the method-specific processing work to the strategy object.
     */
    public String processMethod() {
        return getDonationMethod().process(this);
    }
}
