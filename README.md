# Passport Verification Slot Booking Management System

A complete Java web application based on the supplied project proposal.

## Technology stack
- Java 21
- Jakarta Servlets / JSP
- HTML, CSS, JavaScript
- MySQL
- JDBC
- Apache Tomcat 10.1+
- JSON (REST-style availability endpoint)
- Maven
- VS Code

## Main functions
### Applicant
1. Register
2. Login/logout
3. Dashboard
4. Add/update passport verification details
5. View available slots
6. Book a slot
7. View booking details/status

### Administrator
1. Admin login
2. Dashboard statistics
3. Add verification slots
4. Delete slots that have no booking
5. View all bookings
6. View registered users

## Database
Database script: `database/passport_booking.sql`

Default database configuration is in:
`src/main/java/com/passport/config/DBConnection.java`

Default values:
- Database: `passport_booking`
- User: `root`
- Password: change `YOUR_MYSQL_PASSWORD` to your MySQL password.

## Default administrator
The application creates the admin automatically on first startup if it does not exist:
- Email: `admin@passport.local`
- Password: `Admin@123`

Change this in `AppStartupListener.java` before deployment if you want a different demo password.

## VS Code setup
1. Install JDK 21.
2. Install VS Code.
3. Install the VS Code Extension Pack for Java.
4. Install Maven and make sure `mvn -version` works.
5. Install MySQL Server + MySQL Workbench.
6. Install Apache Tomcat 10.1.x.
7. Open this project folder in VS Code.
8. Edit `DBConnection.java` with your MySQL password.
9. Run `database/passport_booking.sql` in MySQL Workbench.
10. In the VS Code terminal run:
   `mvn clean package`
11. Copy `target/passport-slot-booking.war` into Tomcat's `webapps` folder.
12. Start Tomcat using `bin\startup.bat`.
13. Open:
   `http://localhost:8080/passport-slot-booking/`

## If "mvn is not recognized"
Install Apache Maven and add Maven's `bin` directory to the Windows PATH, then restart VS Code.
Example Maven folder:
`C:\apache-maven-3.9.x\bin`

Verify:
`mvn -version`

## Important
Use Tomcat 10.1+, not Tomcat 9, because this project uses the Jakarta (`jakarta.*`) servlet packages.

## Project phases represented in the code
Phase 1: model classes, constructors, getters/setters, validation, encapsulation.
Phase 2: packages, interfaces, services, authentication and custom exceptions.
Phase 3: JSP/HTML/CSS/JavaScript UI.
Phase 4: Servlets, JSP, sessions and server-side validation.
Phase 5: MySQL schema and JDBC DAO layer.
Phase 6: validation, duplicate booking protection and deployment instructions.

## Real-World Innovations

### 1. Dynamic Digital Appointment Pass with Cryptographic QR Code & Counter Gate Terminal
- **Official Paperless E-Pass (`/app/pass`)**: Printable and mobile-friendly Government Verification Slip featuring a dynamically rendered offline 2D QR matrix, SHA-256 security hash, barcode token, candidate dossier, and reporting time instructions.
- **Officer Gate Scanner & Counter Terminal (`/admin/verify-pass`)**: Desk terminal where passport officers scan or look up appointment references, inspect candidate credentials, and execute progressive clearance stages:
  1. *Gate Check-in* (`CHECKED_IN`)
  2. *Document Inspection* (`DOCS_VERIFIED`)
  3. *Biometrics & Photo Capture* (`BIOMETRICS_DONE`)
  4. *Official Grant / Rejection* (`APPROVED` / `REJECTED`)
- **Full Audit Trail**: Logs timestamp, officer remarks, and stage transitions, while automatically sending live notifications to the applicant.
- **Printable Slip**: Includes `@media print` styling for clean A4 printing without navigation controls.

### 2. Smart Document Pre-Screening Vault & Automated Compliance Engine
- **Zero-Defect Document Vault (`/app/documents`)**: Pre-screening portal where applicants register supporting proofs (Aadhaar / Voter ID, Proof of Date of Birth, Address Proof, Non-ECR Qualification).
- **Algorithmic Format & Checksum Heuristics**:
  - Mathematical **Verhoeff parity checksum validation** on 12-digit Aadhaar numbers.
  - Structural regex validation for Income Tax PAN cards (`[A-Z]{5}[0-9]{4}[A-Z]`).
  - Cross-validation against applicant profile Date of Birth.
  - Automatic masking of sensitive identifiers (`XXXX-XXXX-1234`).
- **Compliance Scoring & Clearance Badge**: Generates a real-time compliance score (0–100%) and "Zero-Defect Certified Counter-Ready" badge to eliminate appointment delays and counter rejections.

### 3. Emergency Tatkal Priority Waitlist & Real-Time Alert Hub
- **Tatkal Emergency Waitlist (`/app/waitlist`)**: When preferred dates are booked, applicants submit priority queue requests categorized by urgency tiers:
  - ⚡ *Tatkal Urgent Expedited*
  - 🏥 *Medical Emergency Priority*
  - 🎓 *Higher Education / Visa Deadline*
  - *Standard Priority Window*
- **Dynamic Queue Ranking**: Automatically assigns priority ranks based on urgency tier and timestamp.
- **One-Click Cancellation & Capacity Restoration (`/app/cancel-booking`)**: Applicants can cancel confirmed bookings, which instantly frees slot capacity and updates database records.
- **Automated Waitlist Reallocation Matcher**: When a booking is cancelled or an admin releases new slots, the backend matching engine scans active waitlist entries and automatically dispatches high-priority alerts to matching applicants.
- **Live In-App Notification Center (`/app/notifications`)**: Dedicated alerts feed with unread status indicators, plus a live animated header badge powered by the REST endpoint `GET /api/notifications/count`.

## Additional Modules
### Smart Passport Assistant
- Verification readiness score based on the existing passport profile.
- Identifies completed and missing profile information.
- Preference-based slot recommendation using Java scoring logic.
- Returns the top three available appointment choices.

### Admin Booking Analytics
- Applicant/application counts, confirmed/cancelled booking counts.
- Future slot and availability counts, plus active waitlist count, verified vault documents, and today's check-ins.
- Most popular booking day, peak booking time, and capacity utilization.

### Passport & Notification JSON APIs
- `GET /api/passport`: Returns logged-in applicant's profile details.
- `GET /api/slots`: Returns real-time slot availability.
- `GET /api/notifications/count`: Returns unread notifications count for live navbar badge polling.

