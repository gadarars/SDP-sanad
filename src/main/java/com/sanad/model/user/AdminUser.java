package com.sanad.model.user;

import java.util.List;

/** MODEL: a ADMIN user. Created only through UserFactory. */
public class AdminUser extends User {
    public AdminUser(int id, String username, String email) { super(id, username, email); }

    @Override public String getRole() { return "ADMIN"; }
    @Override public String getPermissions() { return "Manage everything: users, donations, events, volunteers, beneficiaries"; }
    @Override public List<String> getAllowedTabs() { return List.of("Donations", "Volunteers", "Events", "Beneficiaries", "Users", "Lookup tables"); }
}
