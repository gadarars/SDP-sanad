# Sanad - Non-Profit Management System (CSE336, Phase 1)

Java + JavaFX + SQLite, built with Maven. Team T08 "Sanad".
One system for: donations, volunteers, events, beneficiaries, communication.

## 1. Run it
Needs JDK 17+ and Maven (JavaFX and SQLite download automatically through Maven).

    mvn javafx:run

A window opens with tabs: Donations, Volunteers, Events, Beneficiaries, Users, Lookup tables.
The database file `sanad.db` is created automatically on first run (it is NOT committed to git).

## 2. How the code is organised (MVC)
    com.sanad
    |- App.java            starts the program
    |- db/                 DatabaseConnection  (Singleton - one shared connection)
    |- model/              data + SQL, one package per module
    |   |- donation/ volunteer/ event/ beneficiary/ user/ lookup/
    |   |- notification/   (empty - Strategy + Decorator go here)
    |- view/               JavaFX screens. NEVER touch the database from a view.
    |- controller/         one controller per module: validates input, calls the model

Flow of every feature:  View (button click) -> Controller (validate) -> Repository (SQL) -> database.
To add a new module, copy the Volunteer files (Volunteer, VolunteerRepository,
VolunteerController, VolunteerView), rename, add a table in `db/DatabaseConnection.java`
and a tab in `view/MainView.java`.

## 3. What is already done
| Item | Status |
|---|---|
| Singleton (DB connection) | DONE - `db/DatabaseConnection` |
| MVC | DONE for Donations, Volunteers, Events, Beneficiaries, Users, Lookups |
| Basic CRUD screens | DONE for all 6 (add / update / delete / list) |

## 4. What is left (Phase 1 = 10 marks)
| Item | Marks | Owner | Where |
|---|---|---|---|
| Factory | 0.5 | Member B | `model/donation/` - DonationFactory creates Online/Check/InKind donation by type |
| Observer | 1 | Member C | `model/donation/` + `model/event/` - when a donation/event changes, observers are notified |
| Strategy | 1 | Member D | `model/notification/` - NotificationStrategy: Email / SMS / Social |
| Decorator | 1 | Member D | `model/notification/` - e.g. LoggingDecorator, ReceiptDecorator wrapping a notification |
| User control panel | 1 | Member A | `Users` tab: improve (roles, validation); full CRUD already works |
| CRUD lookup tables | 0.5 | Member A | `Lookup tables` tab: seed donation types, event categories, skills; use them in the combo boxes |
| Client requirements | 0.5 | everyone | pick real flows (see section 6), make sure they work |
| Class diagram | 2 | Member A (merge) + all | `docs/` - all main classes + the Phase 1 patterns ONLY |
| SRS document | - | all, A merges | `docs/` - each member writes the section for their module |
| Grading sheet, zip, repo link | - | Member A | final step |

Do NOT use Phase 2 patterns yet (Command, Adapter, Facade, Template Method, Iterator, State, Proxy).

## 5. Team split (change names if you agree otherwise)
| Member | Owns |
|---|---|
| A - Roaa | Users + Lookup tables, class diagram merge, SRS merge, final packaging |
| B - Yousif (leader) | Donations module + Factory, repo/branch management |
| C - Philopater | Volunteers + Events modules + Observer |
| D - Adham | Beneficiaries + Communication (Strategy + Decorator) |

## 6. Client requirements (examples - pick and finish these)
Think like a real client using the system:
- Staff records a donation and a receipt is produced (Factory + Decorator).
- Staff registers a volunteer, logs hours, assigns them to an event.
- Staff creates an event; registered volunteers are notified when it changes (Observer + Strategy).
- Staff records a beneficiary and what was delivered to them.
- Admin manages users and their roles.
Write the final list in the SRS and test each one end to end.

## 7. Pattern cheat sheet (one line each)
- Factory: one method returns the right object from a type, `DonationFactory.create("online")`.
- Strategy: an interface with several swappable implementations, `NotificationStrategy` -> `EmailStrategy`, `SmsStrategy`.
- Decorator: a wrapper with the same interface that adds behaviour, `LoggingDecorator(new EmailNotification())`.
- Observer: a subject keeps a list of observers and calls `update()` on each when it changes.
- Singleton: private constructor + `getInstance()` (see `DatabaseConnection`).
Mark each pattern class with a comment such as `/** STRATEGY PATTERN */` so the grader can find it.

## 8. Git rules
- Never work directly on `main`. Make your own branch: `git checkout -b feature/<your-feature>`.
- Commit small and often with clear messages, push your branch, then open a Pull Request into `main`.
- Before you start each day: `git checkout main` then `git pull`.
- Do not commit `target/` or `sanad.db` (the `.gitignore` already prevents that - do not delete `.gitignore`).
- Only merge to `main` when `mvn javafx:run` works.

## 9. Final submission checklist
1. SRS (Word or PDF) with all team members' names.
2. Code zip (without `target/`).
3. GitHub repo link in a text file.
4. Grading criteria sheet with names and IDs filled in.
(Check the LMS for the exact requirements.)
