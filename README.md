# bookSphere

A minimalist book reading platform built with Spring Boot and Redis, offering personalized recommendations, JWT-based authentication, and an intuitive infinite-scroll catalog.

## Table of Contents

* [Features](#features)
* [Tech Stack](#tech-stack)
* [Prerequisites](#prerequisites)
* [Getting Started](#getting-started)

  * [Clone Repository](#clone-repository)
  * [Configure Environment Variables](#configure-environment-variables)
  * [Build & Run with Docker Compose](#build--run-with-docker-compose)
* [Configuration](#configuration)
* [Database Schema](#database-schema)
* [API Endpoints](#api-endpoints)
* [Frontend Pages](#frontend-pages)
* [Caching](#caching)
* [Testing](#testing)
* [Contributing](#contributing)
* [License](#license)

## Features

* JWT-based authentication (login, registration, logout)
* Role-based security (via Spring Security)
* Infinite-scroll book catalog with lazy loading
* Personalized recommendations (genre-based)
* Reading history tracking (progress, timestamps)
* Favorites (add/remove, counts)
* User preference management (preferred genres)
* Redis caching for user details and recommendations
* PostgreSQL persistence
* Dockerized environment with Docker Compose

## Tech Stack

* **Backend**: Java 21, Spring Boot 3.4.5
* **Security**: Spring Security, JWT
* **Database**: PostgreSQL, Spring Data JPA
* **Caching**: Redis, Spring Cache (Lettuce)
* **Frontend**: Thymeleaf, Tailwind CSS, vanilla JS
* **Containerization**: Docker, Docker Compose
* **Build Tool**: Gradle

## Prerequisites

* Docker & Docker Compose
* Java 21
* Gradle 8+

## Getting Started

### Clone Repository

```bash
git clone https://github.com/yourusername/bookSphere.git
cd bookSphere
```

### Configure Environment Variables

This project uses a `.env` file to store sensitive settings. Follow these steps:

1. **Copy the example file**:

   ```bash
   cp .env.example .env
   ```
2. **Open `.env`** and fill in your values:

   ```dotenv
   # JWT Settings
   JWT_SECRET=your_super_secret_key
   JWT_EXPIRATION_MS=86400000
   ```
3. **Ensure `.env` is in `.gitignore`** so your secrets aren’t committed.

Docker Compose will automatically load these variables.

### Build & Run with Docker Compose

```bash
docker-compose up --build
```

* **App**: [http://localhost:8080](http://localhost:8080)
* **PostgreSQL**: localhost:5433
* **Redis**: localhost:6379

## Configuration

Configuration is primarily in `src/main/resources/application.yaml`:

```yaml
spring:
  application:
    name: book-sphere
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    open-in-view: false
  data:
    redis:
      host: book-sphere-redis
      port: 6379
      repositories:
        enabled: false

server:
  port: 8080

application:
  security:
    jwt:
      secret-key: ${JWT_SECRET}
      expiration: ${JWT_EXPIRATION_MS:86400000}
```

### Dockerfile

```dockerfile
FROM gradle:8.7.0-jdk21-alpine AS builder
WORKDIR /app
COPY build.gradle settings.gradle gradle.properties ./
COPY src ./src
RUN gradle clean build -x test

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### `docker-compose.yaml`

```yaml
version: '3.8'
services:
  book-sphere-db:
    image: 'postgres:17-alpine'
    environment:
      - POSTGRES_DB=book-sphere
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=password
    ports:
      - "127.0.0.1:5433:5432"
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres -d book-sphere"]
      interval: 5s
      timeout: 5s
      retries: 5
    volumes:
      - postgres-data:/var/lib/postgresql/data

  book-sphere-redis:
    image: 'redis:7-alpine'
    ports:
      - "127.0.0.1:6379:6379"
    command: ["redis-server", "--save", "60", "1"]
    healthcheck:
      test: ["CMD-SHELL", "redis-cli ping"]
      interval: 5s
      timeout: 5s
      retries: 5

  book-sphere-app:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8081:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://book-sphere-db:5432/book-sphere
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=password
      - JWT_SECRET=${JWT_SECRET}
      - JWT_EXPIRATION_MS=${JWT_EXPIRATION_MS:86400000}
      - JAVA_OPTS='-Xmx512m'
    depends_on:
      book-sphere-db:
        condition: service_healthy
      book-sphere-redis:
        condition: service_healthy

volumes:
  postgres-data:
```

## Database Schema

Tables:

* `users`, `role`, `user_roles`, `user_preferred_genre`
* `book`, `genre`, `book_genre`
* `favorite_book`, `rating`, `reading_history`

## API Endpoints

### Authentication

| Method | Endpoint                | Description                |
| ------ | ----------------------- | -------------------------- |
| POST   | `/api/v1/auth/register` | User registration          |
| POST   | `/api/v1/auth/login`    | Authenticate (returns JWT) |
| POST   | `/api/v1/auth/logout`   | Clear JWT cookie           |

### Books & Catalog

| Method | Endpoint                    | Description                           |
| ------ | --------------------------- | ------------------------------------- |
| GET    | `/api/v1/books?page=&size=` | Paginated list of books               |
| GET    | `/api/v1/books/search?q=`   | Search external (OpenLibrary)         |
| GET    | `/books/{id}/read`          | Read book page (iframe or HTML embed) |

### Reading History

| Method | Endpoint                              | Description                            |
| ------ | ------------------------------------- | -------------------------------------- |
| POST   | `/api/v1/reading-history/{bookId}`    | Start reading (creates history record) |
| PUT    | `/api/v1/reading-history/{historyId}` | Update progress and last opened date   |

### Favorites

| Method | Endpoint                               | Description           |
| ------ | -------------------------------------- | --------------------- |
| PUT    | `/api/v1/users/{id}/favorite/{bookId}` | Add to favorites      |
| DELETE | `/api/v1/users/{id}/favorite/{bookId}` | Remove from favorites |

### User Profile

| Method | Endpoint   | Description                  |
| ------ | ---------- | ---------------------------- |
| GET    | `/profile` | HTML profile page with stats |

## Frontend Pages

* **`/`**: Landing page with recommended books
* **`/books`**: Infinite-scroll catalog
* **`/books/{id}/read`**: Reader view with progress tracking
* **`/login`, `/register`**: Auth forms
* **`/profile`**: User dashboard (stats, recent activities)

## Caching

* **`@EnableCaching`** in `BookSphereApplication`
* RedisCacheManager with TTL 30 minutes
* `@Cacheable("users")` on `UserService.loadUserByUsername`

## Testing

* Unit tests: JUnit 5, Mockito
* Integration tests: Spring Boot Test, Testcontainers (PostgreSQL, Redis)
* Run:

```bash
gradle test
```
