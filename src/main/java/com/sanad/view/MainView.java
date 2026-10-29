package com.sanad.view;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/** VIEW: main window with one tab per module. Add a new Tab here for each new screen. */
public class MainView extends TabPane {
    public MainView() {
        getTabs().addAll(
                tab("Donations", new DonationView()),
                tab("Volunteers", new VolunteerView()),
                tab("Events", new EventView()),
                tab("Beneficiaries", new BeneficiaryView()),
                tab("Users", new UserView()),
                tab("Lookup tables", new LookupView()));
    }

    private Tab tab(String title, Node content) {
        Tab t = new Tab(title, content);
        t.setClosable(false);
        return t;
    }
}
