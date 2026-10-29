# Sanad - Non-Profit Management System (CSE336, Phase 1)

Java 17 + JavaFX 21 + MySQL 9.7, built with Maven. Team T08 "Sanad".
One system for: donations, volunteers, events, beneficiaries, communication.

## 1. Run it
Needs JDK 17+ and Docker Desktop. Maven is not needed: use the wrapper (`mvnw`).

    docker compose up -d        # starts MySQL 9.7 (first time downloads the image)
    ./mvnw javafx:run           # Windows CMD / PowerShell: mvnw.cmd javafx:run

A window opens with tabs: Donations, Volunteers, Events, Beneficiaries, Users, Lookup tables.
The tables are created automatically on first run. Data lives in the Docker volume `mysql-data`,
so it survives restarts. `docker compose down -v` wipes it.

Not using Docker? Install MySQL 9.7, create a database `sanad`, and put your own
URL / user / password in `src/main/resources/db.properties`.

## 2. How the code is organised (MVC)
    com.sanad
    |- App.java            starts the program
    |- db/                 DatabaseConnection (Singleton)
    |- model/              data + SQL, one package per module
    |   |- donation/ volunteer/ event/ beneficiary/ user/ lookup/ notification/
    |- view/               JavaFX screens. NEVER touch the database from a view.
    |- controller/         one controller per module: validates input, calls the model

Flow of every feature:  View (button click) -> Controller (validate) -> Repository (SQL) -> database.
To add a new module, copy the Volunteer files (Volunteer, VolunteerRepository,
VolunteerController, VolunteerView), rename, add a table in `db/DatabaseConnection.java`
and a tab in `view/MainView.java`.

## 3. Rules (from the lectures)
- Follow the lecture code for each pattern: same class shapes and method names.
- No `System.out` in model, controller or pattern classes. Only views show output.
- Class diagram: no empty or stand-alone classes, no "System" god class, use interfaces where possible,
  mix association / aggregation / inheritance / realization.
- Do NOT use Phase 2 patterns yet (Command, Adapter, Facade, Template Method, Iterator, State, Proxy).

## 4. Who does what (Phase 1 = 10 marks)
| Member | Module | Patterns |
|---|---|---|
| A | Architecture + Users: Singleton, `UserFactory`, User Control Panel (login + full CRUD for every user type) | Singleton (0.5, done), Factory (0.5) |
| B | Donations: CRUD, donor history | Strategy (1), Decorator (1) |
| C | Events + Volunteers: registration, attendance, assignment, hours, certificate | Observer (1) |
| D | Beneficiaries, notifications, lookup tables, documents | CRUD lookup tables (0.5) |
| All | Own module's MVC, class diagram part, 2-3 client requirements | MVC (2), Class diagram (2), Client req. (0.5) |

Pattern plans:
- **Singleton** (A): `db/DatabaseConnection` - one shared connection, reopened if MySQL drops it.
- **Factory** (A): `UserFactory.create(role, ...)` returns `Donor`, `Volunteer`, `Beneficiary` or `Admin`.
- **Strategy** (B): `DonationMethod` interface with `OnlineDonation`, `CheckDonation`, `InKindDonation`.
  `Donation` holds a `DonationMethod`, has `setDonationMethod(...)` and `performPayment()` delegates to it.
- **Decorator** (B): abstract `DonationPackage` (`getDescription()`, `getAmount()`), base packages
  `OneTimeDonation` / `MonthlySponsorship`, abstract `DonationAddOnDecorator`, add-ons
  `CoverFeesDecorator`, `GiftAidDecorator`, `MatchingGiftDecorator`.
- **Observer** (C): own `Subject` (`registerObserver`, `removeObserver`, `notifyObservers`) and
  `Observer` (`update`) interfaces. `Event` is the subject; attendees and volunteers observe it.
- **Notifications** (D): `NotificationSender` with `EmailSender` / `SmsSender` that save to a
  `notifications` table, shown in a Notifications tab.

## 5. Final submission checklist
1. SRS (Word or PDF) with all team members' names.
2. Code zip (without `target/`).
3. GitHub repo link in a text file.
4. Grading criteria sheet with names and IDs filled in.
(Check the LMS for the exact requirements.)
