package com.sanad.view;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

/**
 * VIEW: main window with one tab per module. Add a new Tab here for each new screen.
 */
public class MainView extends TabPane {
    public MainView() {
        setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        DonationView donations = new DonationView();
        VolunteerView volunteers = new VolunteerView();
        EventView events = new EventView();
        BeneficiaryView beneficiaries = new BeneficiaryView();
        UserView users = new UserView();
        LookupView lookups = new LookupView();
        getTabs().addAll(
                tab("Donations", donations, donations::refresh),
                tab("Volunteers", volunteers, volunteers::refresh),
                tab("Events", events, events::refresh),
                tab("Beneficiaries", beneficiaries, beneficiaries::refresh),
                tab("Users", users, users::refresh),
                tab("Lookup tables", lookups, lookups::refresh));
    }

    // Tab that reloads its view's data each time it is opened
    private static Tab tab(String title, Node view, Runnable refresh) {
        Tab tab = new Tab(title, view);
        tab.setOnSelectionChanged(e -> {
            if (tab.isSelected()) refresh.run();
        });
        return tab;
    }
}
