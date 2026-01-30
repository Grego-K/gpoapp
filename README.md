##  GpoApp - Group Purchasing Organization Application



> Ολοκληρωμένη πλατφόρμα B2B για τη διαχείριση ομαδικών αγορών φαρμακείων με κεντρικές συμφωνίες και τιμές κλίμακας.

---

##  Πίνακας Περιεχομένων

- [Περιγραφή](#-περιγραφή)
- [Αρχιτεκτονική](#️-αρχιτεκτονική)
- [Λειτουργικότητα](#-λειτουργικότητα)
- [Tech Stack](#-tech-stack)
- [Εγκατάσταση & Εκτέλεση](#-εγκατάσταση--εκτέλεση)
- [REST API](#-rest-api)
- [Testing Credentials](#-testing--demo-credentials)
- [Screenshots](#-screenshots)
- [Συνεισφορά](#-συνεισφορά)

---

##  Περιγραφή

Το **GpoApp** δίνει τη δυνατότητα για μαζικές αγορές φαρμακευτικών προϊόντων μέσω μιας σύγχρονης web εφαρμογής.

### Πώς Λειτουργεί

1. **Η GPO διαπραγματεύεται** τις τιμές με προμηθευτές
2. **Εισάγει προϊόντα** και ποσότητες ανά συμβόλαιο
3. **Οι φαρμακοποιοί** υποβάλλουν παραγγελίες με προνομιακές τιμές
4. **Αυτόματη διαχείριση** αποθέματος και παραγγελιών

### Βασικά Χαρακτηριστικά

✅ Διαχείριση καταλόγου προϊόντων & κατηγοριών  
✅ Σύστημα τιμών κλίμακας (GPO Pricing)  
✅ Διαχείριση παραγγελιών & αποθέματος  
✅ Role-based access control (ADMIN/PHARMACIST)  
✅ Πλήρες ιστορικό παραγγελιών  
✅ Responsive UI με Thymeleaf

---

##  Αρχιτεκτονική

Η εφαρμογή ακολουθεί **Layered Architecture** με αυστηρό διαχωρισμό ευθυνών:

```
┌─────────────────────────────────────┐
│   Presentation Layer (Thymeleaf)   │
├─────────────────────────────────────┤
│   Controller Layer (Spring MVC)    │
├─────────────────────────────────────┤
│   Service Layer (Business Logic)   │
├─────────────────────────────────────┤
│   Data Access Layer (JPA/Hibernate)│
├─────────────────────────────────────┤
│          MySQL Database             │
└─────────────────────────────────────┘
```

### Κύρια Components

- **Presentation**: Δυναμική παραγωγή HTML με Thymeleaf templates
- **Controllers**: Διαχείριση HTTP requests και responses
- **Services**: Business logic (υπολογισμοί τιμών, validation)
- **Repositories**: Επικοινωνία με τη βάση δεδομένων
- **Security**: Spring Security 6 με BCrypt encoding

---

##  Λειτουργικότητα

###  Διαχειριστής (ADMIN)

| Λειτουργία                | Περιγραφή |
|---------------------------|-----------|
| **Dashboard**             | Συνολική εποπτεία συστήματος |
| **Διαχείριση Προϊόντων**  | CRUD operations για προϊόντα & κατηγορίες |
| **Διαχείριση Κατηγοριών** | Καταγραφή στοιχείων |
| **GPO Pricing**           | Ορισμός τιμών κλίμακας |

###  Φαρμακοποιός (PHARMACIST)

| Λειτουργία | Περιγραφή |
|-----------|-----------|
| Αναζήτηση Προϊόντων** | Φιλτράρισμα βάσει κατηγορίας/ονόματος |
|  **Καλάθι Αγορών** | Προσθήκη προϊόντων & checkout |
|  **Ιστορικό Παραγγελιών** | Προβολή & παρακολούθηση παραγγελιών |
|  **Ακύρωση** | Ακύρωση παραγγελίας (status: PENDING) |

---

##  Tech Stack

### Backend
- ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.9-6DB33F?logo=springboot&logoColor=white)
- ![Spring Security](https://img.shields.io/badge/Spring%20Security-6-6DB33F?logo=springsecurity&logoColor=white)
- ![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-Hibernate-59666C?logo=hibernate&logoColor=white)

### Frontend
- ![Thymeleaf](https://img.shields.io/badge/Thymeleaf-Templates-005F0F?logo=thymeleaf&logoColor=white)
- ![HTML5](https://img.shields.io/badge/HTML5-E34F26?logo=html5&logoColor=white)
- ![CSS3](https://img.shields.io/badge/CSS3-1572B6?logo=css3&logoColor=white)

### Database
- ![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white)

### Tools
- ![Gradle](https://img.shields.io/badge/Gradle-Build-02303A?logo=gradle&logoColor=white)
- ![JWT](https://img.shields.io/badge/JWT-Auth-000000?logo=jsonwebtokens&logoColor=white)
- ![Figma](https://img.shields.io/badge/Figma-Design-F24E1E?logo=figma&logoColor=white)

---

##  Εγκατάσταση & Εκτέλεση

### Προαπαιτούμενα

-  Java 17+
-  MySQL 8.0
-  Gradle 7.x+ 

###  1. Προετοιμασία Βάσης Δεδομένων

```sql
-- Δημιουργία βάσης
CREATE DATABASE gpoappdb;
```

###  2. Φόρτωση δεδομένων

1. Μεταβείτε στα [Releases](https://github.com/Grego-K/gpoapp/releases)
2. Κατεβάστε το `Dump_sql_data_1.9.0.sql`
3. Εισάγετε το dump:

```bash
# Μέσω MySQL CLI
mysql -u root -p gpoappdb < Dump_sql_data_1.9.0.sql

# Ή μέσω MySQL Workbench: Server → Data Import
```

###  3. Ρύθμιση Εφαρμογής

Επεξεργαστείτε το `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gpoappdb
spring.datasource.username=your_username
spring.datasource.password=your_password
```

###  4. Εκτέλεση

#### Επιλογή Α: Με Gradle

```bash
./gradlew bootRun
```

#### Επιλογή Β: Με JAR

```bash
# Κατεβάστε το gpoapp-1.9.0.jar από τα Releases
java -jar gpoapp-1.9.0.jar
```

###  5. Πρόσβαση

Ανοίξτε τον browser στο: **http://localhost:8080**

---

##  REST API

Το GpoApp διαθέτει REST API με JWT authentication.

### Quick Start

```bash
# 1. Login και λήψη JWT token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"123456789"}'

# 2. Χρήση του token για πρόσβαση
curl -X GET http://localhost:8080/api/products \
  -H "Authorization: Bearer <your-token>"
```

### Βασικά Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/auth/login` | POST | Σύνδεση & λήψη JWT token |
| `/api/users/me` | GET | Προφίλ χρήστη |
| `/api/products` | GET | Κατάλογος προϊόντων |
| `/api/orders` | GET | Ιστορικό παραγγελιών |
| `/pharmacist/orders/add-bulk` | POST | Bulk δημιουργία παραγγελίας |

###  Documentation

- **[Πλήρης API Documentation](docs/API_DOCUMENTATION.md)** - Αναλυτική τεκμηρίωση όλων των endpoints
- **[Swagger UI](http://localhost:8080/swagger-ui/index.html)** - Interactive API testing
- **[OpenAPI Spec](http://localhost:8080/v3/api-docs)** - JSON specification

### Example: Create Order

```javascript
const token = 'your-jwt-token';

fetch('http://localhost:8080/pharmacist/orders/add-bulk', {
  method: 'POST',
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  },
  body: JSON.stringify([
    { productId: 1, quantity: 10 },
    { productId: 2, quantity: 5 }
  ])
})
.then(res => res.json())
.then(data => console.log('Order created:', data));
```

### Security Features

- 🔒 **BCrypt Password Hashing**
- 🎫 **JWT Token-based Authentication** (REST API)
- 👥 **Role-Based Access Control** (RBAC)
- 🔑 **Session Management**

---

##  Testing & Demo Credentials

Χρησιμοποιήστε τους παρακάτω λογαριασμούς για δοκιμές:

| Ρόλος | Username | Password | Περιγραφή |
|-------|----------|----------|-----------|
|  **Admin** | `admin` | `123456789` | Πλήρης πρόσβαση διαχείρισης |
|  **Pharmacist** | `user` | `123456789` | Βασικές λειτουργίες φαρμακοποιού |
|  **Pharmacist** | `user6` | `123456789` | Εναλλακτικός φαρμακοποιός |

> Οι κωδικοί παρέχονται μόνο για development περιβάλλον


---

##  Δομή Project

```
gpoapp/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── gr/aueb/cf/gpoapp/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       ├── model/
│   │   │       ├── dto/
│   │   │       ├── security/
│   │   │       └── config/
│   │   └── resources/
│   │       ├── templates/
│   │       ├── static/
│   │       └── application.properties
│   └── test/
├── gradle/
├── build.gradle
└── README.md
```

---

##  Δημιουργός

**Coding Factory 8 - Final Project - Grego-K**

**2026**

