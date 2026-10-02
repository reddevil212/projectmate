# 🤝 ProjectMate

**ProjectMate** is a modern, RESTful Spring Boot application designed to facilitate team formation, collaborative project management, and skill matching for developers and creators.

---

## 🌟 Key Features

- 🔐 **Stateless JWT Authentication**: Secure user registration, authentication, access tokens, and refresh token rotation.
- 👤 **User Management**: Comprehensive endpoints to search, retrieve, update, and manage user profiles and roles.
- 📁 **Project Management**: Create, update, search, and manage project listings.
- 👥 **Team Member Operations**: Join projects, manage roles (`OWNER`, `MEMBER`), and assign project-specific titles (`FRONTEND DEV`, `BACKEND DEV`, etc.).
- 🛠️ **Master Skill Catalog**: Centralized tech stack lookup table.
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

2. **Configure Database & JWT Settings** (`src/main/resources/application.properties`):
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

#### Register Example Request Body:
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "securePassword123"
}
```

---

### 👤 2. User Management (`/api/users`)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/users` | List all users |
| `GET` | `/api/users/{id}` | Get user details by ID |
| `POST` | `/api/users` | Create user |
| `PUT` | `/api/users/{id}` | Update user details |
| `DELETE` | `/api/users/{id}` | Delete user by ID |
| `GET` | `/api/users/search?name={name}` | Search users by name |
| `GET` | `/api/users/search/email?email={email}` | Search user by email |
| `GET` | `/api/users/search/role?role={role}` | Search users by role |
| `GET` | `/api/users/search/role/in?roles={roles}` | Search users matching a list of roles |
| `GET` | `/api/users/search/role/not-in?roles={roles}` | Search users excluding a list of roles |

---

### 📂 3. Projects (`/api/projects`)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/projects/create` | Create a project (auto-assigns creator as `OWNER`) |
| `GET` | `/api/projects/{id}` | Get project details by ID |
| `GET` | `/api/projects?ownerId={ownerId}` | List all projects owned by a user |
| `GET` | `/api/projects/search?name={name}` | Search projects by name |
| `GET` | `/api/projects/search/type?type={type}` | Search projects by type (Web, Mobile, AI) |
| `GET` | `/api/projects/search/status?status={status}` | Search projects by status (`OPEN`, `IN_PROGRESS`) |
| `GET` | `/api/projects/search/owner-status` | Filter by owner ID and status |
| `PUT` | `/api/projects/update/{id}` | Update project info |
| `DELETE` | `/api/projects/delete/{id}` | Delete project |

#### Create Project Request Body:
```json
{
  "ownerId": 1,
  "name": "ProjectMate Web App",
  "type": "Full Stack",
  "description": "Platform for developer matching and project collaboration.",
  "memberCount": 5,
  "status": "OPEN"
}
```

---

### 👥 4. Project Members (`/api/projects`)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/projects/{projectId}/members` | Add a member (`userId`, optional `role`, `roleInProject`) |
| `GET` | `/api/projects/{projectId}/members` | List all project members |
| `GET` | `/api/projects/{projectId}/members/check?userId={userId}` | Check if user is a member |
| `GET` | `/api/projects/{projectId}/owner/check?userId={userId}` | Check if user is project owner |
| `PUT` | `/api/projects/{projectId}/members/{userId}/role?role={role}` | Update member platform role (`OWNER`, `MEMBER`) |
| `PUT` | `/api/projects/{projectId}/members/{userId}/role-in-project` | Update role title (e.g., `BACKEND DEV`) |
| `DELETE` | `/api/projects/{projectId}/members/{userId}` | Remove member from project |
| `GET` | `/api/projects/members/user/{userId}` | List all projects user belongs to |

---

### 🛠️ 5. Master Skills Catalog (`/api/skills`)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/skills` | Add skill to master catalog |
| `GET` | `/api/skills` | List all skills |
| `GET` | `/api/skills/{id}` | Get skill by ID |
| `GET` | `/api/skills/search?name={name}` | Search skill by name (case-insensitive) |
| `PUT` | `/api/skills/{id}` | Update skill name |
| `DELETE` | `/api/skills/{id}` | Delete skill |

---

### ⚡ 6. Project Tech Stack Requirements (`/api/project-skills`)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/project-skills` | Link required skill to project |
| `GET` | `/api/project-skills/{id}` | Get project skill requirement by ID |
| `GET` | `/api/project-skills/project/{projectId}` | Get all skills required for a project |
| `GET` | `/api/project-skills/skill/{skillId}` | Get all projects requiring a skill |
| `GET` | `/api/project-skills/project/{projectId}/level/{level}` | Filter required skills by level |
| `PUT` | `/api/project-skills/{id}?level={level}` | Update level requirement |
| `DELETE` | `/api/project-skills/{id}` | Remove requirement |
| `DELETE` | `/api/project-skills/project/{projectId}/skill/{skillId}` | Remove skill from project |

#### Add Project Skill Request Body:
```json
{
  "projectId": 1,
  "skillId": 2,
  "level": "Intermediate"
}
```

---

### 👔 7. Project Roles Catalog (`/api/project-roles`)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/project-roles` | Create a standard project role title |
| `GET` | `/api/project-roles` | Get all available roles |
| `GET` | `/api/project-roles/{id}` | Get role by ID |
| `GET` | `/api/project-roles/search?name={name}` | Search role by name |
| `PUT` | `/api/project-roles/{id}` | Update role name |
| `DELETE` | `/api/project-roles/{id}` | Delete role |

---

### 🤖 8. AI Assistance & Project Generation (`/api/ai`)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/ai/analyze` | Analyze project prompt & return structured project plan, skills, and 4 team roles |
| `POST` | `/api/ai/create-project?ownerId={ownerId}` | Analyze prompt & automatically create & persist project, skills, and roles in DB |
| `POST` | `/api/ai/save-analysis?ownerId={ownerId}` | Persist an existing AI project analysis object into the database |

---

### 🔔 9. Notification Management (`/api/notifications`)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/notifications` | Create a user notification |
| `GET` | `/api/notifications/user/{userId}` | Get all non-deleted notifications for a user |
| `GET` | `/api/notifications/user/{userId}/unread` | Get unread notifications for a user |
| `GET` | `/api/notifications/user/{userId}/unread-count` | Get count of unread notifications |
| `PUT` | `/api/notifications/{id}/read?userId={userId}` | Mark notification as read |
| `PUT` | `/api/notifications/user/{userId}/read-all` | Mark all notifications as read for a user |
| `DELETE` | `/api/notifications/{id}?userId={userId}` | Soft delete notification |

---

### 📩 10. Invitation Management (`/api/invitations`)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/invitations` | Send a project invitation (triggers notification to receiver) |
| `GET` | `/api/invitations/receiver/{receiverId}` | Get all invitations received by user |
| `GET` | `/api/invitations/receiver/{receiverId}/pending` | Get pending invitations received by user |
| `GET` | `/api/invitations/sender/{senderId}` | Get all invitations sent by user |
| `GET` | `/api/invitations/project/{projectId}` | Get all invitations for a project |
| `PUT` | `/api/invitations/{id}/accept?receiverId={receiverId}` | Accept invitation (auto-adds member to project & notifies sender) |
| `PUT` | `/api/invitations/{id}/reject?receiverId={receiverId}` | Decline invitation (notifies sender) |
| `PUT` | `/api/invitations/{id}/cancel?senderId={senderId}` | Cancel pending invitation |

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
└── service         # Business Logic Services
```

---

## 📄 License

This project is licensed under the MIT License.
Made with ❤️ by [reddevil212](https://github.com)
