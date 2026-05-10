# University Gym Management System

SE 324 – Software Construction | Spring 2026
University of Prince Mugrin (UPM), Madinah, Saudi Arabia

A JavaFX + MySQL desktop application that digitalizes the management of gym memberships and sports facility bookings at UPM. Replaces the current manual WhatsApp-based process with a structured, role-based digital platform.

---

## Project Overview

### Problem
Gym membership and facility booking at UPM are currently managed informally through WhatsApp messages and paper records. This causes lost requests, double-bookings, no audit trail, and no easy way to verify entry at the gate.

### Solution
A multi-role desktop application that:
- Digitalizes membership registration, renewal, and cancellation
- Provides a visual booking calendar with approval workflow
- Generates QR receipts for entry verification
- Gives the coach a dashboard to manage requests and view analytics
- Gives security staff a fast way to verify entry at the gate

### Key Features
- Role-based login (Student, Faculty, Coach, Staff, Security)
- Membership management with 3-day refund window
- Facility booking with 12-hour cancellation rule and 2-hour max duration
- Visual weekly calendar showing facility availability
- Coach approve/reject workflow for booking requests
- Security entry verification with QR-coded receipts
- Reports dashboard with key metrics and per-facility breakdown
- Layered architecture (UI → Controller → Service → DAO → MySQL)
- Configuration via local `db.properties` (gitignored, per-developer)

---

## Team Members

| Name | Student ID | Role                                   |
|---|---|----------------------------------------|
| Mohsen Al Masud | 4410097 | Frontend Lead, Testing & Documentation |
| Ahmed Hakimi | 4320010 | Backend Lead                           |
| Amged Elhag | 4411651 | Frontend Lead, Testing & Documentation |

**Instructor:** Dr. Osama Qaeed

---

## Technology Stack

| Component | Technology |
|---|---|
| Language | Java 23 |
| GUI Framework | JavaFX 23.0.2 |
| Database | MySQL 8.0 |
| DB Access | JDBC (mysql-connector-j 9.1.0) |
| Build Tool | Maven 3.8+ |
| Testing | JUnit 5.11 |
| QR Generation | ZXing 3.5.3 |

---

## Architecture

The system follows a 5-layer architecture with strict separation of concerns:
┌──────────────────────────────────────────────┐
│  View Layer (FXML files in resources/fxml/)  │
├──────────────────────────────────────────────┤
│  Controller Layer (JavaFX event handlers)    │
├──────────────────────────────────────────────┤
│  Service Layer (business logic and rules)    │
├──────────────────────────────────────────────┤
│  DAO Layer (SQL queries via JDBC)            │
├──────────────────────────────────────────────┤
│  MySQL Database                              │
└──────────────────────────────────────────────┘

Controllers never call DAOs directly. Services validate business rules (refund windows, time conflicts, role permissions) before delegating to DAOs.

### Project Structure

upm-gym-management/
├── database/
│   ├── schema.sql              # Table definitions
│   └── sample_data.sql         # Seed data for testing
├── docs/
│   └── PROJECT_HANDOFF.md      # Internal team handoff notes
├── src/
│   ├── main/
│   │   ├── java/com/upm/gym/
│   │   │   ├── controller/     # JavaFX controllers
│   │   │   ├── dao/            # Data access objects
│   │   │   ├── enums/          # Role, BookingStatus
│   │   │   ├── exception/      # Custom exceptions
│   │   │   ├── model/          # Entity classes
│   │   │   ├── service/        # Business logic
│   │   │   ├── util/           # DBConnection helper
│   │   │   └── Main.java       # Application entry point
│   │   └── resources/
│   │       ├── css/            # Stylesheets (future)
│   │       ├── fxml/           # 15 FXML view files
│   │       └── db.properties.example
│   └── test/                   # JUnit tests
├── pom.xml                     # Maven configuration
└── README.md

---

## Screens (15 total)

| # | Screen | User Role | Description |
|---|---|---|---|
| 1 | Login | All | Authenticates user and routes to role-appropriate dashboard |
| 2 | Member Dashboard | Student / Faculty | Hub with 4 navigation buttons |
| 3 | Coach Dashboard | Coach / Staff | Hub with 4 navigation buttons |
| 4 | Register Membership | Member | Form for choosing plan (1 Month / Semester / Year) |
| 5 | Payment | Member | Card details with order summary, last 4 stored |
| 6 | Receipt with QR | Member | Confirmation with scannable QR for gate entry |
| 7 | My Membership | Member | View current membership, cancel with 3-day refund window |
| 8 | Booking Calendar | Member | Weekly grid showing facility availability |
| 9 | New Booking | Member | Form pre-filled from calendar, validates 2-hour max |
| 10 | My Bookings | Member | Table of all bookings with 12-hour cancel window |
| 11 | Pending Bookings | Coach | Coach review queue with approve/reject |
| 12 | Approved Bookings | Coach | Today's approved bookings, read-only |
| 13 | Member Lookup | Coach | Search by ID, see member info and membership status |
| 14 | Reports | Coach | 4 stat cards plus per-facility booking counts |
| 15 | Security Search | Security | Verify entry by ID, big VALID/NOT VALID indicator |

---

## Getting Started

### Prerequisites

- Java 23 (Eclipse Adoptium recommended)
- MySQL 8.0 or later
- Maven 3.8 or later
- IntelliJ IDEA (recommended) or any IDE with Maven and JavaFX support
- Git

### 1. Clone the Repository

```bash
git clone https://github.com/amgedupm/upm-gym-management.git
cd upm-gym-management
```

### 2. Set Up the Database

Open MySQL and run the schema and seed scripts:

```bash
mysql -u root -p < database/schema.sql
mysql -u root -p < database/sample_data.sql
```

This creates the `upm_gym` database with three tables (`users`, `memberships`, `bookings`) and populates them with test accounts and sample bookings.

### 3. Configure Database Connection

Each developer keeps their MySQL credentials in a local config file. Copy the template:

```bash
cp src/main/resources/db.properties.example src/main/resources/db.properties
```

Then open `db.properties` and set your password:

```properties
db.url=jdbc:mysql://localhost:3306/upm_gym
db.user=root
db.password=YOUR_MYSQL_PASSWORD_HERE
```

This file is gitignored, so it stays local to your machine.

### 4. Build and Run

```bash
mvn clean install
mvn javafx:run
```

The login window should appear. Use any of the test accounts below to explore.

---

## Test Accounts

These accounts are seeded into the database by `sample_data.sql`:

| User ID | Password | Role | Notes |
|---|---|---|---|
| `4410097` | `1234` | STUDENT | Has active 1 Semester membership and approved bookings |
| `4413828` | `1234` | STUDENT | Has active 1 Year membership and pending booking |
| `4510353` | `1234` | FACULTY | Has expired 1 Month membership (good for testing expiry logic) |
| `coach01` | `coach123` | COACH | Reviews and approves bookings |
| `staff01` | `staff123` | STAFF | Helper account (routes to Coach Dashboard) |
| `sec01` | `sec123` | SECURITY | Routes directly to Security Search screen |

---

## How to Test the System

The system can be tested through scenarios that exercise different roles and flows. Each scenario below walks through the expected behavior end-to-end.

### Scenario 1 — Member Registration and Payment

1. Log in as `4413828` / `1234`
2. Click **Register Membership**
3. Choose plan "1 Semester" and a future start date
4. Click **Subscribe** → navigates to Payment screen
5. Enter card holder name, 16-digit card number, expiry MM/YY, 3-digit CVV
6. Click **Pay Now** → navigates to Receipt screen with scannable QR code
7. Scan the QR with a phone camera to verify it encodes the receipt details
8. Click **Done** → returns to Member Dashboard

**Expected:** Receipt generated with reference number RCP-XXXXXX. Database records the membership.

### Scenario 2 — Member Books a Facility

1. Log in as `4410097` / `1234`
2. Click **Book a Field**
3. Booking Calendar opens showing the current week with colored slots
4. Click any green "Free" cell → New Booking form opens pre-filled with that facility, date, and start time
5. Click **Submit Booking Request**
6. Auto-redirects to Member Dashboard

**Expected:** New row appears in `bookings` table with status `PENDING`.

### Scenario 3 — Coach Approves Booking

1. Log in as `coach01` / `coach123`
2. Click **Pending Booking Requests**
3. Select any pending booking → Approve and Reject buttons enable
4. Click **Approve** → confirmation dialog → OK
5. Booking disappears from pending list
6. Click **Approved Bookings** → the approved booking appears here (if dated today)

**Expected:** Database row updated to `status = APPROVED`.

### Scenario 4 — Security Verifies Entry

1. Log in as `sec01` / `sec123`
2. Lands directly on Security Search (no dashboard)
3. Enter `4410097` and click **Verify**
4. If user has an approved booking today → green "VALID ENTRY" with booking details
5. Enter `9999999` → red "USER NOT FOUND"
6. Enter `coach01` → red "NOT VALID — NO BOOKING TODAY"

**Expected:** Color-coded status reflects whether entry is allowed.

### Scenario 5 — Member Cancels Membership

1. Log in as `4410097` / `1234`
2. Click **My Membership**
3. Verify status shows "Active" with X days remaining (green)
4. Click **Cancel Membership**
5. Confirmation dialog mentions whether the user is within the 3-day refund window
6. Click OK

**Expected:** Status changes to "Cancelled" (red), button disables.

### Scenario 6 — Member Cancels a Booking

1. Log in as `4410097` / `1234`
2. Click **My Bookings**
3. Select an approved future booking (more than 12 hours away)
4. Cancel button enables → click it → confirm
5. Try selecting an approved booking less than 12 hours away → Cancel button stays disabled (FR-11)

**Expected:** Cancellation only allowed within the rule window.

### Scenario 7 — Coach Reviews Reports

1. Log in as `coach01` / `coach123`
2. Click **Reports**
3. View 4 stat cards: Total Members, Active Memberships, Bookings Today, Pending Requests
4. View per-facility breakdown showing bookings by Football Field and Basketball Court

**Expected:** Numbers reflect real database state. Refresh by leaving and re-entering.

### Scenario 8 — Member Lookup

1. Log in as `coach01` / `coach123`
2. Click **Member Lookup**
3. Search `4410097` → details panel shows name, role, and membership status (active/expired)
4. Search `4510353` → shows expired membership in red
5. Search `9999999` → "No member found"

**Expected:** Color-coded membership status helps coach decide whether to allow access.

---

## Validation Rules Implemented

The following business rules are enforced in the UI and service layer:

| Rule | Where |
|---|---|
| Booking duration max 2 hours | New Booking form |
| Booking date must be in the future | New Booking form |
| End time must be after start time | New Booking form, BookingService |
| Cancellation only up to 12 hours before booking | My Bookings (FR-11) |
| Membership refund eligibility within 3 days of start | My Membership (FR-05) |
| Card number must be exactly 16 digits | Payment form |
| CVV must be exactly 3 digits | Payment form |
| Expiry format MM/YY with valid month 01-12 | Payment form |
| Past time slots are not selectable in calendar | Booking Calendar |
| Approved/pending slots are not selectable in calendar | Booking Calendar |

---

## Running Unit Tests

```bash
mvn test
```

Tests cover the service layer (business logic) and use stub data to avoid hitting the database during test runs.

---

## Configuration Notes

### Java Version
The project requires Java 23. Older versions cause module loading errors with JavaFX 23.

### JavaFX Version
JavaFX is set to 23.0.2 in `pom.xml`. Versions 24+ have module incompatibilities with Java 23.

### Maven Plugin
The `javafx-maven-plugin` is configured with `<executable>${java.home}/bin/java</executable>` to ensure it uses the correct Java version when multiple JDKs are installed.

### Database Credentials
Never commit `db.properties` to Git. The `.gitignore` already excludes it. Each developer maintains their own copy.

---

## Known Limitations

- Passwords are stored in plain text (not hashed). For a production system, BCrypt or similar would be required.
- Payment processing is simulated locally (no real bank integration).
- The QR code on receipts encodes data only; it is not cryptographically signed.
- The calendar shows 1-hour granularity slots from 9 AM to 7 PM; finer granularity would require schema and UI changes.

These are documented in the project report under "Future Enhancements."

---

## Configuration Management (Git Workflow)

The team follows a feature-branch workflow:

1. Each feature is developed on a branch named `feature/<descriptor>` (e.g. `feature/booking-calendar`).
2. Branches are pushed to GitHub and reviewed via Pull Requests.
3. After review, PRs are merged into `main`.
4. The `main` branch always contains a working, deployable state.

Conventional Commit messages are used (`feat:`, `fix:`, `docs:`, `refactor:`, etc.) to keep history readable.

---

## Documentation

- **Project Plan and Requirements:** `docs/SE324_Master_Plan.docx`
- **Internal Team Handoff:** `docs/PROJECT_HANDOFF.md`

---

## License

This project is developed for academic purposes at the University of Prince Mugrin as part of SE 324 — Software Construction (Spring 2026).