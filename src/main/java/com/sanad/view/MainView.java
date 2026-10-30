package com.sanad.view;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import com.sanad.auth.Session;
import com.sanad.model.user.Feature;
import com.sanad.model.user.User;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;

/**
 * VIEW: main window with a sidebar entry per module. Add a new Section here for each new screen.
 */
public class MainView extends BorderPane {

    private record Section(String title, Ikon icon, Feature feature, CrudView<?> view) {
    }

    private boolean dark;

    /**
     * Shows only the sections the logged-in user's type is allowed to open.
     */
    public MainView(Runnable onLogout) {
        User user = Session.getInstance().getCurrentUser();
        applyTheme();
        List<Section> sections = List.of(
                new Section("Donations", Feather.GIFT, Feature.DONATIONS, new DonationView()),
                new Section("Volunteers", Feather.USERS, Feature.VOLUNTEERS, new VolunteerView()),
                new Section("Events", Feather.CALENDAR, Feature.EVENTS, new EventView()),
                new Section("Beneficiaries", Feather.HEART, Feature.BENEFICIARIES, new BeneficiaryView()),
                new Section("Users", Feather.SHIELD, Feature.USERS, new UserView()),
                new Section("Lookup tables", Feather.LIST, Feature.LOOKUPS, new LookupView()));
        ListView<Section> nav = new ListView<>(FXCollections.observableArrayList(
                sections.stream().filter(s -> user.getFeatures().contains(s.feature())).toList()));
        nav.getStyleClass().add(Tweaks.EDGE_TO_EDGE);
        nav.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Section s, boolean empty) {
                super.updateItem(s, empty);
                setText(empty ? null : s.title());
                setGraphic(empty ? null : new FontIcon(s.icon()));
            }
        });
        // the selected screen reloads its data each time it is opened
        nav.getSelectionModel().selectedItemProperty().addListener((o, old, s) -> {
            if (s == null) return;
            setCenter(s.view());
            s.view().refresh();
        });
        nav.getSelectionModel().selectFirst();

        Label brand = new Label("Sanad");
        brand.getStyleClass().addAll("brand", Styles.TITLE_3);

        Label who = new Label(user.getUsername() + " (" + ViewUtil.pretty(user.getRole()) + ")");
        who.getStyleClass().add(Styles.TEXT_MUTED);
        Button logout = new Button("Log out", new FontIcon(Feather.LOG_OUT));
        logout.getStyleClass().add(Styles.FLAT);
        logout.setOnAction(e -> onLogout.run());

        VBox sidebar = new VBox(12, brand, nav, who, themeToggle(), logout);
        sidebar.getStyleClass().add("sidebar");
        VBox.setVgrow(nav, Priority.ALWAYS);
        setLeft(sidebar);
    }

    private Button themeToggle() {
        Button toggle = new Button("Dark mode", new FontIcon(Feather.MOON));
        toggle.getStyleClass().add(Styles.FLAT);
        toggle.setOnAction(e -> {
            dark = !dark;
            applyTheme();
            toggle.setText(dark ? "Light mode" : "Dark mode");
            toggle.setGraphic(new FontIcon(dark ? Feather.SUN : Feather.MOON));
        });
        return toggle;
    }

    private void applyTheme() {
        Application.setUserAgentStylesheet((dark ? new PrimerDark() : new PrimerLight()).getUserAgentStylesheet());
    }
}
