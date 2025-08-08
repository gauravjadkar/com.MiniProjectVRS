# com.MiniProjectVRS


### 🚗 Vehicle Rental System

A full-stack web application for renting vehicles online. This system allows customers to register, log in, browse vehicles, book rentals, and manage their profile — including a **"Watch Later"** feature for shortlisting vehicles.

---

## 📌 Features

- ✅ Customer Registration & Login
- ✅ Browse Available Vehicles by Type & Date
- ✅ Book Vehicle Rentals
- ✅ "Watch Later" Vehicle Shortlisting
- ✅ View Rental History
- ✅ Customer Profile Page
- ✅ Logout Functionality

---

## 🛠 Tech Stack

### 🔹 Frontend
- HTML, CSS, JavaScript

### 🔹 Backend
- Java with Spring Boot
- RESTful APIs
- JPA/Hibernate (for database interaction)

### 🔹 Database
- PostgreSQL

---

## 📂 Project Structure

org.System/<br>
├── src/main/resources/<br>
│ ├── index.html<br>
│ ├── profile.html<br>
│ ├── watchlater.html<br>
│ └── script.js<br>
├── src/main/java/org/<br>
│ ├── system/ # Controllers and services<br>
│ ├── repositories/ # Spring Data JPA Repositories<br>
│ ├── Entities/ # JPA Entities like Customer, Vehicle, Rental<br>
│ └── VehicleRentalApplication.java<br>
├── src/main/resources/<br>
│ └── application.properties<br>
└── README.md

---
spring.datasource.url=jdbc:mysql://localhost:3306/vehicle_rental<br>
spring.datasource.username=root<br>
spring.datasource.password=yourpassword<br>
spring.jpa.hibernate.ddl-auto=update<br>
---
### Open Frontend/
register.html

 ---
 | Endpoint                                   | Method | Description           |
| ------------------------------------------ | ------ | --------------------- |
| `/api/VehicleRentalSystem/vehicles`        | GET    | Get all vehicles      |
| `/api/VehicleRentalSystem/register`        | POST   | Register new customer |
| `/api/VehicleRentalSystem/login`           | POST   | Authenticate customer |
| `/api/VehicleRentalSystem/book`            | POST   | Book vehicle          |
| `/api/VehicleRentalSystem/watchlater/{id}` | GET    | Get watch later list  |

 
## 🔒 Authentication
  - localStorage.setItem("customer_id", custId);
---
## 📷 Snapshot
[![](https://github.com/gauravjadkar/com.MiniProjectVRS/blob/main/Screenshot%202025-08-08%20195444.png)]
---
## 🪶 Author Details
### College : 
Solapur  Education Society,solapur
### Group Members :
- Gaurav Jadkar
- Koushik Chilveri
- Abhishek Mhetre
- Arjun Uttarkar
- Arya Haridas

#### Made for Educational Purpose 




