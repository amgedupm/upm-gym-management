# University Gym Management System
SE 324 – Software Construction | Spring 2026
University of Prince Mugrin (UPM) Madinah, Saudi Arabia

---
 
## Project Overview

The UPM Gym Management System is a desktop application that digitalizes and automates the management of gym memberships and sports facility bookings at the University of Prince Mugrin. It replaces the current manual, WhatsApp-based process with a structured, role-based digital platform.

### Key Features
- Digital membership registration, renewal, and cancellation
- Online payment processing (Al Rajhi, Al Bilad, Al Riyadh, Alinma Banks)
- Sports facility booking with coach approval workflow
- Role-based access (Students, Faculty, Coach, Gym Staff, Security)
- Security gate verification for approved bookings
- Reporting dashboard for gym management

---

## Team Members

| Name | Student ID |
|------|-----------|
| Mohsen Al Masud | 4410097 |
| Amged Elhag | 4411651 |
| Ahmed Hakimi | 4320010 |

**Instructor:** Dr. Osama Qaeed

---

## Technology Stack

| Component | Technology |
|-----------|-----------|
| Language | Java 23 |
| GUI Framework | JavaFX |
| Database | MySQL |
| DB Access | JDBC |
| Build Tool | Maven |
| Testing | JUnit 5 |

---

## Architecture

The system follows a 3-Layer MVC Architecture:

- Model Layer          # Entity classes (User, Membership, Booking, etc.)
- DAO Layer            # Data Access Objects (DB queries)
- Service Layer        # Business logic layer
- Controller Layer     # JavaFX controllers (FXML)
- View Layer           # FXML UI files
- Util Layer           # Helper utilities

---

## Getting Started

### Prerequisites
- Java 23
- MySQL 8.0+
- Maven 3.8+
- JavaFX SDK

### 1. Clone the Repository
git clone https://github.com/amgedupm/upm-gym-management.git
cd upm-gym-management

### 2. Set Up the Database
mysql -u root -p
source database/schema.sql
source database/sample_data.sql

### 3. Configure Database Connection
cp src/main/resources/db.properties.example src/main/resources/db.properties

### 4. Build and Run
mvn clean install
mvn javafx:run

---

## Running Tests
mvn test

---

## User Roles

| Role | Access |
|------|--------|
| Student / Faculty | Register membership, book facilities, view status |
| Gym Coach | Approve bookings, manage members, view reports |
| Gym Staff | Verify memberships, view today's bookings |
| Security Staff | Read-only view of today's approved bookings |

---

## License
This project is developed for academic purposes at the University of Prince Mugrin.
