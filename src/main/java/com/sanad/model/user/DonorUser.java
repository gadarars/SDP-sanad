package com.sanad.model.user;

import java.util.List;

/** MODEL: a DONOR user. Created only through UserFactory. */
public class DonorUser extends User {
    public DonorUser(int id, String username, String email) { super(id, username, email); }

    @Override public String getRole() { return "DONOR"; }
    @Override public String getPermissions() { return "Make donations and view own receipts"; }
    @Override public List<String> getAllowedTabs() { return List.of("Donations"); }
}
