# Sanad - Non-Profit Management System

CSE336 Software Design Patterns, Phase 1. Team T08 "Sanad".

One desktop app for a non-profit's donations, volunteers, events, beneficiaries and communication.
Built with Java 17, JavaFX 21 and MySQL 9.7.

## Run it

You need JDK 17+ and Docker Desktop (running). Maven comes with the repo through the wrapper.

```bash
./mvnw javafx:run
```

On Windows use `.\mvnw.cmd javafx:run` in PowerShell, or `mvnw javafx:run` in CMD.

That one command compiles the code, starts MySQL in Docker and opens the app.
The first run downloads the MySQL image, so it takes longer.
The app creates its tables on first start.

| To | Run |
|---|---|
| Stop MySQL | `docker compose down` |
| Wipe all data | `docker compose down -v` |

MySQL keeps running after you close the app. Data lives in the Docker volume `mysql-data`, so it survives restarts.

**Without Docker:** install MySQL 9.7, create a database called `sanad`, put your URL, user and password in
`src/main/resources/db.properties`, then run `./mvnw javafx:run -Dexec.skip`.

## Code layout (MVC)

```
com.sanad
├── App.java        starts the program
├── db/             DatabaseConnection (Singleton)
├── model/          data classes + repositories (SQL), one package per module
├── controller/     one per module: validates input, calls the repository
└── view/           JavaFX tabs, one per module
```

Every feature follows the same path:

```
View (button click) -> Controller (validate) -> Repository (SQL) -> MySQL
```

Views never talk to the database directly.

**Adding a module:** copy the four Volunteer files (`Volunteer`, `VolunteerRepository`, `VolunteerController`,
`VolunteerView`) and rename them. Then add its table in `db/DatabaseConnection.java` and its tab in `view/MainView.java`.

## Team rules

- Write each pattern the way the lecture does: same class shapes and method names.
- No `System.out` in model, controller or pattern classes. Only views show output.
- Class diagram: no empty or stand-alone classes, no "System" god class. Use interfaces where possible and
  mix association, aggregation, inheritance and realization.
- No Phase 2 patterns yet (Command, Adapter, Facade, Template Method, Iterator, State, Proxy).

## Who does what

| Member | Module | Graded items |
|---|---|---|
| A | Users: login and User Control Panel (full CRUD for every user type) | Singleton 0.5 (done), Factory 0.5, User Control Panel 1 |
| B | Donations: CRUD and donor history | Strategy 1, Decorator 1 |
| C | Events and volunteers: registration, attendance, assignment, hours, certificates | Observer 1 |
| D | Beneficiaries, notifications, lookup tables, documents | CRUD lookup tables 0.5 |
| All | Their own module's MVC, class diagram part and 2-3 client requirements | MVC 2, Class diagram 2, Client requirements 0.5 |

## Pattern plans

| Pattern | Owner | Classes |
|---|---|---|
| Singleton | A | `db/DatabaseConnection`: one shared connection, reopened if MySQL drops it |
| Factory | A | `UserFactory.create(role, ...)` returns a `Donor`, `Volunteer`, `Beneficiary` or `Admin` |
| Strategy | B | `DonationMethod` interface, implemented by `OnlineDonation`, `CheckDonation`, `InKindDonation`. `Donation` holds one, swaps it with `setDonationMethod(...)` and calls it from `performPayment()` |
| Decorator | B | Abstract `DonationPackage` (`getDescription()`, `getAmount()`). Base packages `OneTimeDonation`, `MonthlySponsorship`. Abstract `DonationAddOnDecorator` with `CoverFeesDecorator`, `GiftAidDecorator`, `MatchingGiftDecorator` |
| Observer | C | Own `Subject` (`registerObserver`, `removeObserver`, `notifyObservers`) and `Observer` (`update`) interfaces. `Event` is the subject; attendees and volunteers observe it |
| Notifications | D | `NotificationSender` with `EmailSender` and `SmsSender`, which save to a `notifications` table shown in a Notifications tab |

## Submission checklist

1. SRS (Word or PDF) with every member's name.
2. Code zip, without `target/`.
3. Text file with the GitHub repo link.
4. Grading criteria sheet with names and IDs filled in.

Check the LMS for the exact requirements.
