package com.sanad.model.beneficiary;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * MODEL: a resource or service given to a beneficiary.
 */
public class Distribution {
    private int id;
    private int beneficiaryId;
    private String item;
    private BigDecimal quantity;
    private LocalDate givenOn;

    public Distribution(int id, int beneficiaryId, String item, BigDecimal quantity, LocalDate givenOn) {
        this.id = id;
        this.beneficiaryId = beneficiaryId;
        this.item = item;
        this.quantity = quantity;
        this.givenOn = givenOn;
    }

    public int getId() {
        return id;
    }

    public int getBeneficiaryId() {
        return beneficiaryId;
    }

    public String getItem() {
        return item;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public LocalDate getGivenOn() {
        return givenOn;
    }
}
