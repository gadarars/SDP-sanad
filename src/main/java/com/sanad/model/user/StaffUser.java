package com.sanad.model.user;

import java.util.List;

/** MODEL: a STAFF user. Created only through UserFactory. */
public class StaffUser extends User {
    public StaffUser(int id, String username, String email) { super(id, username, email); }

    @Override public String getRole() { return "STAFF"; }
    @Override public String getPermissions() { return "Manage donations, events, volunteers and beneficiaries"; }
    @Override public List<String> getAllowedTabs() { return List.of("Donations", "Volunteers", "Events", "Beneficiaries", "Lookup tables"); }
}
