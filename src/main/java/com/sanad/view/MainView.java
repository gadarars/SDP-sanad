package com.sanad.view;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/** VIEW: main window with one tab per module. Add a new Tab here for each new screen. */
public class MainView extends TabPane {
    public MainView() {
        setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        getTabs().addAll(
                new Tab("Donations", new DonationView()),
                new Tab("Volunteers", new VolunteerView()),
                new Tab("Events", new EventView()),
                new Tab("Beneficiaries", new BeneficiaryView()),
                new Tab("Users", new UserView()),
                new Tab("Lookup tables", new LookupView()));
    }
}
