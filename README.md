# 🎵 Music Store — Spring Boot Web Application

A full-stack Spring Boot application for browsing music albums, managing a shopping cart, placing orders, and administering users, albums, and artists.  
The project uses Thymeleaf for server-side rendering, MySQL (dev/prod) or H2 (tests), OpenFeign for inter-service communication, Kafka for user-related events, and OpenPDF to export orders as PDF.

## 🚀 Tech Stack

### **Backend**
- Spring Boot 3.4  
  - Web  
  - Thymeleaf  
  - Data JPA  
  - Validation  
  - Security  
  - Actuator  
  - Cache  
  - Scheduling  
  - Async  
- Java 17  
- Maven

### **Persistence**
- MySQL (dev/prod)  
- H2 in-memory (tests)  
- Hibernate ORM

### **Messaging**
- Apache Kafka (Spring for Kafka)

### **HTTP Clients**
- Spring Cloud OpenFeign

### **View Layer**
- Thymeleaf + `thymeleaf-extras-springsecurity6`

### **PDF Export**
- OpenPDF (librepdf)

### **Testing**
- Spring Boot Test  
- Spring Security Test  
- H2 Test Database  
- API / Integration Tests  

## ✨ Features

### **Public**
- Home page  
- Catalog browsing  
- Album details  

### **Authentication**
- Custom login / registration  
- Form login with Spring Security  

### **User Area**
- View & edit profile  
- Shopping cart: add / remove / clear  
- Checkout flow  
- My Orders  
- Order details  
- **Export orders to PDF**  
- Manage notification preferences (via Notification Service microservice)

### **Admin Area**
- Admin dashboard (stats: revenue, albums, orders, users)  
- Manage users (toggle role/status)  
- Manage albums  
- Manage artists  

### **System Features**
- Caching (Spring Cache)  
- Scheduled tasks (order status updates)  
- Domain events  
- Kafka events (user registered)  
- Asynchronous processing  

## 📁 Project Structure

```
src/main/java/app
│
├── Application.java        # Entry point; enables scheduling, caching, Feign
│
├── config/                 # MVC config, Kafka config, Bean config
│
├── event/                  # Domain events (OrderCreatedEvent), listeners
│
├── exception/              # Global and domain exception handling
│
├── export/                 # PDF export utility (OpenPDF)
│
├── init/                   # Data seeders (active only in dev profile)
│
├── mapper/                 # DTO ↔ Entity mappers
│
├── model/
│   ├── dto/                # Data Transfer Objects
│   ├── entity/             # ORM entities
│   ├── enums/              # Domain enums
│   └── projection/         # JPQL projections
│
├── notification/           # OpenFeign client + DTOs
│
├── repository/             # Spring Data JPA repositories
│
├── scheduler/              # Scheduled jobs (order status updates)
│
├── security/               # Spring Security config, roles, access rules
│
├── service/                # Business logic (albums, orders, cart, users)
│
└── web/                    # Controllers, advice, MVC helpers
```

`src/main/resources` contains:
- Thymeleaf templates (`home`, `catalog`, `album`, `cart`, `checkout`, `orders`, `admin`)  
- Static resources (CSS, images)  
- `application-dev.properties`, `application-prod.properties`, `application-test.properties`

## 🛠 Prerequisites

- Java 17  
- Maven 3.9+  
- MySQL 8.x running  
  - Dev credentials: `root / soft-uni`  
  - URL: `jdbc:mysql://localhost:3306/music_store_app?createDatabaseIfNotExist=true`  
- (Optional) Kafka at `localhost:9092`  
- (Optional) Notification Service at `http://localhost:8082/api/v1/notifications`

## ⚙️ Configuration & Profiles

### **Profiles**
- `dev` (default) → MySQL localhost, Kafka localhost  
- `prod` → MySQL via `host.docker.internal`  
- `test` → H2 database  

### **Key Properties**
- `server.port=8081`
- `spring.jpa.hibernate.ddl-auto=update`
- `notification-svc.base-url=http://localhost:8082/api/v1/notifications`
- `spring.kafka.bootstrap-servers=localhost:9092`

## ▶️ Running the Application (Development)

1. Start MySQL (and optionally Kafka & Notification Service)
2. Run the application:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

3. Open:

```
http://localhost:8081
```

## 👤 Seeded Users (dev profile only)

| Role  | Username  | Password |
|-------|-----------|----------|
| Admin | Emil777   | 456123   |
| User  | Petko777  | 456123   |

## 📦 Building & Running (Production)

Build:

```bash
mvn clean package
```

Run:

```bash
java -jar target/music-store-app-1.0.1.jar --spring.profiles.active=prod
```

## 🧪 Testing

Run all tests:

```bash
mvn test
```

The test profile uses H2 in-memory DB and mocks external services when needed.

## 🔗 Key Endpoints (Partial List)

### **Public**
- `GET /home` — Homepage  
- `GET /catalog` — All albums  
- `GET /album/{id}` — Album details  
- `GET /login`, `GET /register`, `POST /register`  

### **Authenticated**
- `/users/profile` — Profile  
- `/cart` — View cart  
- `/cart/add`, `/cart/remove`, `/cart/clear`  
- `/checkout`  
- `/my-orders`  
- `/my-orders/export/pdf`  

### **Admin**
- `/admin/admin-dashboard`  
- `/admin/users`  
- `/admin/users/{id}/status` (PUT)  
- `/admin/users/{id}/role` (PUT)  

## 🌐 External Integrations

### **Notification Service (Feign Client)**
- Base URL from property: `notification-svc.base-url`
- Endpoints used:  
  - send notifications  
  - manage preferences  
  - fetch history  
- Feign client: `app.notification.client.NotificationClient`

### **Kafka**
- Topic: `USER_REGISTERED_EVENT_V1`
- Producer: `UserRegisteredEventProducer`

## 🔐 Security

- Form login with Spring Security  
- Custom login page  
- Roles: `USER`, `ADMIN`  
- `@PreAuthorize` for role-restricted controllers  
- Static resources fully permitted  

## 🧩 Troubleshooting

- Port 8081 in use → change `server.port`  
- MySQL errors → check credentials and DB availability  
- Kafka offline → app runs, but events fail  
- Notification service offline → UI shows fallback messages  

## 📄 License

Educational / demo project.
