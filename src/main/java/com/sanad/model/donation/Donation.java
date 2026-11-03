package com.sanad.model.donation;

import org.jdbi.v3.core.mapper.reflect.JdbiConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * MODEL: data object for one donation.
 * STRATEGY PATTERN (Context): holds a DonationMethod and delegates method-specific work to it.
 * DECORATOR PATTERN (Concrete Component): the donation that its add-ons wrap.
 */
public class Donation extends DonationComponent {
    private int id;
    private Integer userId;
    private String donorName;
    private BigDecimal amount;
    private DonationMethod donationMethod;
    private List<AddOn> addOns;

    public Donation(int id, Integer userId, String donorName, BigDecimal amount, DonationMethod donationMethod,
                    List<AddOn> addOns) {
        description = "Donation";
        this.id = id;
        this.userId = userId;
        this.donorName = donorName;
        this.amount = amount;
        this.donationMethod = donationMethod;
        this.addOns = List.copyOf(addOns);
    }

    /** Builds a donation from a stored row: the method's code and the add-on codes. */
    @JdbiConstructor
    public Donation(int id, Integer userId, String donorName, BigDecimal amount, String method, String addOns) {
        this(id, userId, donorName, amount, DonationMethods.byName(method), AddOn.parse(addOns));
    }

    @Override
    public BigDecimal cost() {
        return amount;
    }

    public int getId() {
        return id;
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

    /** The chosen add-ons in the order they wrap the donation; one may appear more than once. */
    public List<AddOn> getAddOns() {
        return addOns;
    }

    public void setAddOns(List<AddOn> addOns) {
        this.addOns = List.copyOf(addOns);
    }

    /** Stored form of the add-ons, e.g. "GIFT_WRAPPING,THANK_YOU_CARD". */
    public String getAddOnCodes() {
        return AddOn.codes(addOns);
    }

    public String performProcess() {
        return donationMethod.process(this);
    }
}
