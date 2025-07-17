# 🧾 SplitEase Backend

**SplitEase** is a group expense tracking backend application built with **Spring Boot**. It simplifies the process of
managing and splitting bills among groups with flexible logic and secure authentication.

## 🚀 Features

- ✅ **User Authentication** using **JWT** with Refresh Token support
- 🔐 **Spring Security 6** with stateless, token-based authorization
- 🧮 **Advanced Expense Splitting**
    - Supports Equal, Percentage, and Custom shares
    - Handles multiple users per group and auto-generates settlements
- 📦 **Redis Caching** for improved performance
- 📄 **Custom Exception Handling** for consistent error responses
- 📊 **Spring Actuator** endpoints (enabled in dev only)
- 🧪 **Swagger/OpenAPI** documentation at `/swagger-ui`
- 🧱 Clean architecture with **DTO**, **Service**, and **Repository** separation
- 🐳 **Docker Compose** integration for Redis

## 🛠️ Tech Stack

| Layer      | Technology                     |
|------------|--------------------------------|
| Language   | Java 21                        |
| Framework  | Spring Boot 3.x                |
| Auth       | JWT (access + refresh tokens)  |
| Security   | Spring Security 6              |
| Database   | JPA/Hibernate (PostgreSQL)     |
| Caching    | Redis                          |
| Docs       | SpringDoc OpenAPI (Swagger UI) |
| Deployment | Docker + Docker Compose        |
| Build Tool | Maven                          |

## 📂 Project Structure

```html
src/
├── controller/         # REST controllers (User, Auth, Group)
├── dto/                # Data Transfer Objects
│   ├── request/        # Request payloads
│   └── response/       # Response payloads
├── entity/             # JPA entities
├── exception/          # Custom exceptions and handlers
├── repository/         # Spring Data JPA repositories
├── security/           # JWT filters, config
├── service/            # Service interfaces
│   └── impl/           # Service implementations
├── util/               # Utility classes (e.g., JwtUtil & Pair)
```

## 🧪 API Documentation

> Swagger UI available at:  
> **`http://localhost:8000/swagger-ui`**

## ⚙️ Running Locally

### 1. Clone the Repository

```bash
git clone https://github.com/your-org/splitease-backend.git
cd splitease-backend
```

### 2. Start Containers

```bash
docker-compose up -d
```

### 3. Run the Spring Boot App

```bash
./mvnw spring-boot:run
```

## 🧹 To-Do / Future Enhancements

- Email Verification with Spring Mail
- Group Invite & Join Mechanism
- Scheduled Cleanup for Old Data
- Dockerize full app (Backend + DB + Redis)

## 📜 License

MIT License — feel free to use, modify, and share.