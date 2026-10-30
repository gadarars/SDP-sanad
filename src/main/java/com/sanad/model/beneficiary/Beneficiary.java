package com.sanad.model.beneficiary;

/**
 * MODEL: a person or family that receives help.
 */
public class Beneficiary {
    private int id;
    private String name;
    private String need;
    private String contact;
    private Integer userId;

    public Beneficiary(int id, String name, String need, String contact, Integer userId) {
        this.id = id;
        this.name = name;
        this.need = need;
        this.contact = contact;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNeed() {
        return need;
    }

    public String getContact() {
        return contact;
    }

    public Integer getUserId() {
        return userId;
    }
}
