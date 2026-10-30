package com.sanad.model.user;

import java.util.List;

/** MODEL: a BENEFICIARY user. Created only through UserFactory. */
public class BeneficiaryUser extends User {
    public BeneficiaryUser(int id, String username, String email) { super(id, username, email); }

    @Override public String getRole() { return "BENEFICIARY"; }
    @Override public String getPermissions() { return "View own services and requests"; }
    @Override public List<String> getAllowedTabs() { return List.of("Beneficiaries"); }
}
