package com.sanad.model.user;

import java.util.List;

/** MODEL: a VOLUNTEER user. Created only through UserFactory. */
public class VolunteerUser extends User {
    public VolunteerUser(int id, String username, String email) { super(id, username, email); }

    @Override public String getRole() { return "VOLUNTEER"; }
    @Override public String getPermissions() { return "Register for events and view own hours"; }
    @Override public List<String> getAllowedTabs() { return List.of("Volunteers", "Events"); }
}
