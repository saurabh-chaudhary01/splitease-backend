# 🧾 SplitEase Backend

**SplitEase** is a group expense tracking application that simplifies splitting bills among multiple users. This backend
service is built using **Spring Boot**, offering robust authentication, security, and RESTful APIs.

---

## 🚀 Features

- ✅ **User Authentication** with **JWT + Refresh Token**
- 🔐 **Spring Security** integrated with stateless token-based auth
- 🧾 **Expense Splitting Logic**
    - Equal, Percentage, and Custom Shares
    - Handles multi-user group expenses with settlement generation
- 🧠 **DTO Pattern** to decouple entity models from API responses
- 📦 **Custom Exception Handling** with consistent error responses
- 🧪 **Swagger/OpenAPI Documentation** available at `/swagger-ui`
- 💡 **Modular Structure** (Controller → Service → Repository)
- 🛡️ **Secure Endpoints** with role-based access control
- 📊 **Actuator Endpoints** for health & info (dev only)

---

## 🛠️ Tech Stack

| Layer         | Technology                      |
|---------------|---------------------------------|
| Language      | Java 21                         |
| Framework     | Spring Boot 3.x                 |
| Auth          | JWT (access & refresh tokens)   |
| Security      | Spring Security 6               |
| Documentation | SpringDoc OpenAPI (Swagger UI)  |
| Database      | JPA/Hibernate (plug in your DB) |
| Build Tool    | Maven                           |

---

## 📂 Project Structure

```html

<pre>
src/
├── controller/         # REST controllers (User, Auth, Group)
├── dto/                # Data Transfer Objects
│   ├── request/        # Request payloads
│   └── response/       # Response payloads
├── entity/             # JPA entities
├── exception/          # Custom exceptions and handlers
├── repository/         # Spring Data JPA repositories
├── security/           # JWT filters, config, token utils
├── service/            # Service interfaces
│   └── impl/           # Service implementations
├── util/               # Utility classes (e.g., ObjectMapper config)
</pre>
```

## 📚 API Documentation

Swagger UI is available at:

> 📍 [`http://localhost:8000/swagger-ui`](http://localhost:8080/swagger-ui)