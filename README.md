# 🎵 Music Store (Spring Boot Project)

> ⚠️ **Note:** This is a *temporary README template*.  
> Once the project is fully completed, it will be revised with final details, screenshots, and deployment instructions.  
> (Don’t forget to update sections marked with 📝)

---

## 📖 Overview
Music Store is a full-featured web application built with **Spring Boot** and **Thymeleaf**,  
designed to simulate a modern online music shop.  
Users can browse albums, add them to a shopping cart, and place orders,  
while admins can manage albums, artists, and view sales data.

📝 _This description should be expanded later to include the final scope of your project._

---

## 🚀 Features

### 👤 User Features
- Browse albums by genre
- View detailed album information
- Add albums to a shopping cart
- Proceed to checkout and place orders
- View and edit profile information
- Secure login/register functionality

### 🔐 Admin Features
- Manage albums, artists, and users
- View and filter all orders
- Access admin dashboard

### ⚙️ Common Features
- Session-based shopping cart with real-time counter
- Filtering and sorting (by genre, year, etc.)
- Responsive Thymeleaf-based UI
- PostgreSQL database with Flyway migrations
- Role-based access control

📝 _Add or adjust the feature list when everything is finalized._

---

## 🧠 Technologies Used

| Layer | Technologies |
|--------|---------------|
| Backend | Java 21, Spring Boot 3.4, Spring MVC, Spring Security |
| Database | PostgreSQL, Flyway |
| Frontend | Thymeleaf, HTML5, CSS3 |
| Tools | Maven, IntelliJ IDEA, Git, GitHub |
| Testing | JUnit 5, Mockito |

---

## 🧱 Project Structure

```text
app/
 ├─ web/
 │   ├─ controller/
 │   ├─ advice/
 │   └─ util/
 ├─ service/
 ├─ repository/
 ├─ model/
 │   ├─ entity/
 │   ├─ dto/
 │   └─ enums/
 ├─ util/
 ├─ config/
 └─ security/
Ems1s