# 🏦 Shu Bank
### *One step to save money*

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?style=flat-square)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat-square)
![REST API](https://img.shields.io/badge/API-REST-lightgrey?style=flat-square)
![Status](https://img.shields.io/badge/Status-Live-success?style=flat-square)

A full-featured backend banking system built with Spring Boot, offering secure account management, fund transfers, fixed deposits, and PDF statement generation.

🔗 **Live API:** [YOUR_DEPLOYED_LINK_HERE]  
📁 **GitHub:** [YOUR_GITHUB_REPO_LINK_HERE]

---

## ✨ Features

### Core Banking
- ✅ Create & manage bank accounts
- ✅ Check account balance
- ✅ Deposit & withdraw money
- ✅ Fund transfer (NEFT / RTGS / IMPS / UPI)
- ✅ Full transaction history
- ✅ Mini statement (last 5 transactions)
- ✅ Full statement with **PDF download**
- ✅ Delete / close account
- ✅ Update account details

### Extra Features
- ✅ Fixed Deposit (FD) creation & management

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.x |
| Database | MySQL 8.0 |
| ORM | Spring Data JPA / Hibernate |
| Build Tool | Maven |
| API Type | RESTful APIs |

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- MySQL 8.0+
- Maven 3.8+

### Setup & Run

```bash
# 1. Clone the repository
git clone [YOUR_GITHUB_REPO_LINK_HERE]
cd banking_app_backend

# 2. Configure database
# Open src/main/resources/application.properties
# Update the following:
spring.datasource.url=jdbc:mysql://localhost:3306/shubank
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

# 3. Create the database
mysql -u root -p
CREATE DATABASE shubank;

# 4. Run the application
mvn spring-boot:run
```

The server will start at `http://localhost:8080`

---

## 📡 API Endpoints

### Account Management
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/accounts/create` | Create a new account |
| GET | `/api/accounts/{accountNumber}` | Get account details |
| GET | `/api/accounts/{accountNumber}/balance` | Check balance |
| PUT | `/api/accounts/{accountNumber}/update` | Update account details |
| DELETE | `/api/accounts/{accountNumber}/delete` | Close account |

### Transactions
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/transactions/deposit` | Deposit money |
| POST | `/api/transactions/withdraw` | Withdraw money |
| POST | `/api/transactions/transfer` | Fund transfer |
| GET | `/api/transactions/{accountNumber}/history` | Full transaction history |
| GET | `/api/transactions/{accountNumber}/mini` | Mini statement (last 5) |
| GET | `/api/transactions/{accountNumber}/statement/pdf` | Download PDF statement |

### Fixed Deposit
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/fd/create` | Create fixed deposit |
| GET | `/api/fd/{accountNumber}` | Get FD details |

---

## 📂 Project Structure

```
banking_app_backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/shubank/
│   │   │       ├── controller/      # REST Controllers
│   │   │       ├── service/         # Business Logic
│   │   │       ├── repository/      # JPA Repositories
│   │   │       ├── model/           # Entity Classes
│   │   │       └── dto/             # Data Transfer Objects
│   │   └── resources/
│   │       └── application.properties
├── pom.xml
└── README.md
```

---

## 👨‍💻 Author

**Shubham Kumar Saw**  
📧 shubhamkuumarsaw120@gmail.com
🔗 [YOUR_LINKEDIN_HERE]  
💻 [YOUR_GITHUB_HERE]

---

> *Built as part of a Java Full Stack Development journey.*