# 🤝 ProjectMate

**ProjectMate** is a modern, RESTful Spring Boot application designed to facilitate team formation, collaborative project management, and skill matching for developers and creators.

---

## 🌟 Key Features

- 🔐 **Stateless JWT Authentication**: Secure user registration, authentication, access tokens, and refresh token rotation.
- 👤 **User Profile & Social Links**: Manage bio/about, GitHub link, LinkedIn link, and roles.
- 📸 **Cloudinary Profile Picture Uploads**: Upload profile pictures and project assets directly to Cloudinary CDN and store secure links in database.
- 📁 **Project Management**: Create, update, search, manage project listings, track project `visibility` (`PUBLIC`/`PRIVATE`), and broadcast `latestUpdate` notes.
- 👥 **Team Member Operations**: Join projects, manage roles (`OWNER`, `MEMBER`), and assign project-specific titles (`FRONTEND DEV`, `BACKEND DEV`, etc.).
- 🛠️ **Master Skill Catalog**: Centralized tech stack lookup table.
- 🌟 **User Skills Profile**: Map skills and proficiency ratings to user profiles for team matching.
- ⚡ **Project Tech Stack Matching**: Map required skills and proficiency levels (`Beginner`, `Intermediate`, `Expert`) to projects.
- 👔 **Project Roles Catalog**: Manage standardized open team positions across projects.
- 🤖 **AI Project Analysis & Generation**: Use Google AI Studio Gemini API (`gemini-2.5-flash`) to analyze project prompts, generate structured project specs with 4 team member roles and tech stack requirements, and automatically persist projects to the database.
- 🔔 **In-App Notifications**: Real-time user notifications for invitations, team updates, and read status tracking.
- 📩 **Project Invitations**: Send, receive, accept, decline, and cancel project invitations with automatic team member onboarding.

---

## 🛠️ Technology Stack

- **Java**: 17 / 21
- **Framework**: Spring Boot 3.x
- **Security**: Spring Security + JWT (`jjwt-api` 0.12.x) + BCrypt
- **ORM & Database**: Spring Data JPA / Hibernate, H2 / MySQL / PostgreSQL
- **Media Storage**: Cloudinary Java SDK
- **Utilities**: Lombok, ModelMapper
- **Build Tool**: Gradle

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Gradle 8+

### Setup & Execution

1. **Clone the repository**:
   ```bash
   git clone https://github.com/reddevil212/projectmate.git
   cd projectmate
   ```

2. **Configure Database, JWT & Cloudinary Settings** (`src/main/resources/application.properties`):
   ```properties
   spring.application.name=projectmate

   # Database settings
   spring.datasource.url=jdbc:h2:mem:projectmatedb
   spring.datasource.driverClassName=org.h2.Driver
   spring.datasource.username=sa
   spring.datasource.password=
   spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
   spring.jpa.hibernate.ddl-auto=update

   # JWT Configuration
   jwt.secret=${JWT_SECRET:YOUR_SECURE_JWT_SECRET_KEY}
   jwt.expiration-ms=86400000
   jwt.refresh-expiration-ms=604800000

   # Gemini AI Studio Configuration
   gemini.api.key=${GEMINI_API_KEY:}
   gemini.api.model=gemini-2.5-flash

   # Cloudinary Media Storage
   cloudinary.cloud-name=${CLOUDINARY_CLOUD_NAME:demo}
   cloudinary.api-key=${CLOUDINARY_API_KEY:}
   cloudinary.api-secret=${CLOUDINARY_API_SECRET:}
   ```

3. **Build the Application**:
   ```bash
   ./gradlew build
   ```

4. **Run the Application**:
   ```bash
   ./gradlew bootRun
   ```

---

## 🔒 Authentication

All protected endpoints require a JWT Bearer token in the `Authorization` header:

```http
Authorization: Bearer <your_jwt_access_token>
```

---

## 📖 API Documentation

### 🔐 1. Authentication (`/api/auth/v1`)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/auth/v1/register` | Register a new user |
| `POST` | `/api/auth/v1/login` | Log in and receive access + refresh tokens |
| `POST` | `/api/auth/v1/refresh` | Refresh access token using refresh token |
| `POST` | `/api/auth/v1/logout` | Invalidate refresh token |

---

### 👤 2. User Management (`/api/users`)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/users` | List all users |
| `GET` | `/api/users/{id}` | Get user details by ID |
| `POST` | `/api/users` | Create user |
| `PUT` | `/api/users/{id}` | Update user details (`about`, `profilePic`, `githubLink`, `linkedinLink`) |
| `POST` | `/api/users/{id}/profile-pic` | Upload user profile picture to Cloudinary and update DB |
| `POST` | `/api/users/upload-image` | Upload any image to Cloudinary and return CDN URL |
| `DELETE` | `/api/users/{id}` | Delete user by ID |
| `GET` | `/api/users/search?name={name}` | Search users by name |
| `GET` | `/api/users/search/email?email={email}` | Search user by email |

---

### 📂 3. Projects (`/api/projects`)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/projects/create` | Create a project (supports `visibility`, `latestUpdate`) |
| `GET` | `/api/projects/{id}` | Get project details by ID |
| `GET` | `/api/projects?ownerId={ownerId}` | List all projects owned by a user |
| `GET` | `/api/projects/search?name={name}` | Search projects by name |
| `GET` | `/api/projects/search/type?type={type}` | Search projects by type |
| `GET` | `/api/projects/search/visibility?visibility={visibility}` | Search projects by visibility (`PUBLIC`, `PRIVATE`) |
| `GET` | `/api/projects/search/status?status={status}` | Search projects by status (`OPEN`, `IN_PROGRESS`) |
| `PUT` | `/api/projects/update/{id}` | Update project info (including `visibility` and `latestUpdate`) |
| `DELETE` | `/api/projects/delete/{id}` | Delete project |

---

### 📸 4. Image Upload Service (Cloudinary)

- Integrated via `ImageUploader` service and Cloudinary Java SDK.
- Uploads images securely to Cloudinary CDN and stores returned HTTPS links in database columns (`user_info.profile_pic`).

---

## 📁 Project Structure

```
com.proj.mate
├── config          # Application & Spring Security Configuration
├── controller      # REST Controllers
├── dto             # Request & Response Data Transfer Objects
├── entity          # JPA Entities
├── repository      # Spring Data JPA Repositories
├── security        # JWT Filter & Details
└── service         # Business Logic & ImageUploader Services
```

---

## 📄 License

This project is licensed under the MIT License.
Made with ❤️ by [reddevil212](https://github.com)
