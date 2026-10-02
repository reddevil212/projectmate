# 📖 ProjectMate API Reference

Welcome to the **ProjectMate API Reference**. This document provides comprehensive details for every REST API endpoint, including descriptions, parameters, HTTP methods, headers, and request/response examples.

---

## 🌐 General Information

- **Base URL**: `http://localhost:8080`
- **Content-Type**: `application/json`
- **Authentication**: Bearer Token via HTTP Header (for protected endpoints):
  ```http
  Authorization: Bearer <your_jwt_access_token>
  ```

---

## 📑 Table of Contents

1. [Authentication APIs (`/api/auth/v1`)](#1-authentication-apis)
2. [User Management APIs (`/api/users`)](#2-user-management-apis)
3. [Project Management APIs (`/api/projects`)](#3-project-management-apis)
4. [Project Member APIs (`/api/projects`)](#4-project-member-apis)
5. [Master Skill Catalog APIs (`/api/skills`)](#5-master-skill-catalog-apis)
6. [Project Tech Stack Requirements APIs (`/api/project-skills`)](#6-project-tech-stack-requirements-apis)
7. [Project Roles Catalog APIs (`/api/project-roles`)](#7-project-roles-catalog-apis)
8. [AI Assistance & Project Generation APIs (`/api/ai`)](#8-ai-assistance--project-generation-apis)
9. [Notification Management APIs (`/api/notifications`)](#9-notification-management-apis)
10. [Invitation Management APIs (`/api/invitations`)](#10-invitation-management-apis)
11. [User Skills Profile APIs (`/api/user-skills`)](#11-user-skills-profile-apis)
12. [Health Check APIs (`/health`, `/api/health`)](#12-health-check-apis)

---

## 1. Authentication APIs

### 1.1 Register User
- **Method**: `POST`
- **Endpoint**: `/api/auth/v1/register`
- **Description**: Registers a new user account with hashed password and generates initial JWT access & refresh tokens.
- **Auth Required**: No

#### Request:
```json
{
  "name": "Sayan Pal",
  "email": "sayan@example.com",
  "password": "Password@123"
}
```

#### Response (`201 Created`):
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYXlhbkBleGFtcGxlLmNvbSIsImlhdCI6MTY5NTc0NDAwMCwiZXhwIjoxNjk1ODMwNDAwfQ...",
  "refreshToken": "d8f1e2a3-4b5c-6d7e-8f9a-0b1c2d3e4f5a",
  "tokenType": "Bearer",
  "user": {
    "id": 1,
    "name": "Sayan Pal",
    "email": "sayan@example.com",
    "createdAt": "2026-09-26T10:30:00"
  }
}
```

---

### 1.2 Login User
- **Method**: `POST`
- **Endpoint**: `/api/auth/v1/login`
- **Description**: Authenticates existing credentials and returns fresh access and refresh tokens.
- **Auth Required**: No

#### Request:
```json
{
  "email": "sayan@example.com",
  "password": "Password@123"
}
```

#### Response (`200 OK`):
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYXlhbkBleGFtcGxlLmNvbSIsImlhdCI6MTY5NTc0NDAwMCwiZXhwIjoxNjk1ODMwNDAwfQ...",
  "refreshToken": "d8f1e2a3-4b5c-6d7e-8f9a-0b1c2d3e4f5a",
  "tokenType": "Bearer",
  "user": {
    "id": 1,
    "name": "Sayan Pal",
    "email": "sayan@example.com",
    "createdAt": "2026-09-26T10:30:00"
  }
}
```

---

### 1.3 Refresh Access Token
- **Method**: `POST`
- **Endpoint**: `/api/auth/v1/refresh`
- **Description**: Generates a new access token using a valid refresh token.
- **Auth Required**: No

#### Request:
```json
{
  "refreshToken": "d8f1e2a3-4b5c-6d7e-8f9a-0b1c2d3e4f5a"
}
```

#### Response (`200 OK`):
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYXlhbkBleGFtcGxlLmNvbSIsImlhdCI6MTY5NTc0NDEwMCwiZXhwIjoxNjk1ODMwNTAwfQ...",
  "refreshToken": "d8f1e2a3-4b5c-6d7e-8f9a-0b1c2d3e4f5a",
  "tokenType": "Bearer",
  "user": {
    "id": 1,
    "name": "Sayan Pal",
    "email": "sayan@example.com",
    "createdAt": "2026-09-26T10:30:00"
  }
}
```

---

### 1.4 Logout User
- **Method**: `POST`
- **Endpoint**: `/api/auth/v1/logout`
- **Description**: Invalidates the active refresh token for a user.
- **Auth Required**: Yes

#### Request Body:
```json
1
```

#### Response (`200 OK`):
```json
{}
```

---

## 2. User Management APIs

### 2.1 Get All Users
- **Method**: `GET`
- **Endpoint**: `/api/users`
- **Description**: Retrieves a list of all registered users in the system.
- **Auth Required**: Yes

#### Response (`200 OK`):
```json
[
  {
    "id": 1,
    "name": "Sayan Pal",
    "email": "sayan@example.com",
    "about": "Full stack developer",
    "profilePic": "https://res.cloudinary.com/demo/image/upload/v1234567/sample.jpg",
    "githubLink": "https://github.com/reddevil212",
    "linkedinLink": "https://linkedin.com/in/sayanpal",
    "createdAt": "2026-09-26T10:30:00"
  }
]
```

---

### 2.2 Get User By ID
- **Method**: `GET`
- **Endpoint**: `/api/users/{id}`
- **Description**: Retrieves detailed information for a user by their user ID.
- **Auth Required**: Yes

---

### 2.3 Create User
- **Method**: `POST`
- **Endpoint**: `/api/users`
- **Description**: Creates a new user record with optional profile details (`about`, `profilePic`, `githubLink`, `linkedinLink`).
- **Auth Required**: Yes

#### Request:
```json
{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "password": "Password@123",
  "about": "Frontend Specialist",
  "githubLink": "https://github.com/janedoe",
  "linkedinLink": "https://linkedin.com/in/janedoe"
}
```

---

### 2.4 Update User
- **Method**: `PUT`
- **Endpoint**: `/api/users/{id}`
- **Description**: Updates user profile details including name, email, about bio, profile picture URL, and social links.
- **Auth Required**: Yes

#### Request:
```json
{
  "name": "Jane Doe Updated",
  "about": "Senior Full Stack Architect",
  "githubLink": "https://github.com/janedoe-updated",
  "linkedinLink": "https://linkedin.com/in/janedoe-updated"
}
```

---

### 2.5 Upload Profile Picture
- **Method**: `POST`
- **Endpoint**: `/api/users/{id}/profile-pic`
- **Description**: Uploads a profile picture file to Cloudinary and automatically updates the `profile_pic` URL in the `user_info` database table.
- **Content-Type**: `multipart/form-data`
- **Auth Required**: Yes

#### Request (Form-Data):
- `file`: MultipartFile image

#### Response (`200 OK`):
```json
{
  "id": 1,
  "name": "Sayan Pal",
  "email": "sayan@example.com",
  "profilePic": "https://res.cloudinary.com/demo/image/upload/v1695744000/profile_1.jpg"
}
```

---

### 2.6 Upload Generic Image
- **Method**: `POST`
- **Endpoint**: `/api/users/upload-image`
- **Description**: Uploads any image file to Cloudinary and returns the generated secure CDN URL.
- **Content-Type**: `multipart/form-data`
- **Auth Required**: Yes

#### Response (`200 OK`):
```json
{
  "url": "https://res.cloudinary.com/demo/image/upload/v1695744000/sample.jpg"
}
```

---

### 2.7 Delete User
- **Method**: `DELETE`
- **Endpoint**: `/api/users/{id}`
- **Description**: Deletes a user by their ID.
- **Auth Required**: Yes

---

### 2.8 Search Users By Name
- **Method**: `GET`
- **Endpoint**: `/api/users/search?name={name}`
- **Description**: Searches users matching a name substring (case-insensitive).
- **Auth Required**: Yes

---

### 2.9 Get User By Email
- **Method**: `GET`
- **Endpoint**: `/api/users/search/email?email={email}`
- **Description**: Retrieves a user matching the specified email address.
- **Auth Required**: Yes

---

## 3. Project Management APIs

### 3.1 Create Project
- **Method**: `POST`
- **Endpoint**: `/api/projects/create`
- **Description**: Creates a new project with optional `visibility` (`PUBLIC`, `PRIVATE`) and `latestUpdate` note, automatically assigning `ownerId` as `OWNER`.
- **Auth Required**: Yes

#### Request:
```json
{
  "ownerId": 1,
  "name": "ProjectMate Platform",
  "type": "Web Application",
  "description": "Collaborative project matching for developers.",
  "memberCount": 5,
  "visibility": "PUBLIC",
  "latestUpdate": "Initial release of backend APIs",
  "status": "OPEN"
}
```

#### Response (`200 OK`):
```json
{
  "id": 6,
  "owner": {
    "id": 1,
    "name": "Sayan Pal",
    "email": "sayan@example.com"
  },
  "name": "ProjectMate Platform",
  "type": "Web Application",
  "description": "Collaborative project matching for developers.",
  "memberCount": 5,
  "visibility": "PUBLIC",
  "latestUpdate": "Initial release of backend APIs",
  "createdAt": "2026-09-26T11:00:00",
  "status": "OPEN"
}
```

---

### 3.2 Get Project By ID
- **Method**: `GET`
- **Endpoint**: `/api/projects/{id}`
- **Description**: Retrieves details for a specific project by its ID.
- **Auth Required**: Yes

---

### 3.3 List Projects By Owner ID
- **Method**: `GET`
- **Endpoint**: `/api/projects?ownerId={ownerId}`
- **Description**: Returns all projects created by a specific owner ID.
- **Auth Required**: Yes

---

### 3.4 Search Projects By Name
- **Method**: `GET`
- **Endpoint**: `/api/projects/search?name={name}`
- **Description**: Searches projects by a substring matching their name (case-insensitive).
- **Auth Required**: Yes

---

### 3.5 Search Projects By Type
- **Method**: `GET`
- **Endpoint**: `/api/projects/search/type?type={type}`
- **Description**: Filters projects by category/type (e.g., `Web Application`, `Mobile`, `AI`).
- **Auth Required**: Yes

---

### 3.6 Search Projects By Visibility
- **Method**: `GET`
- **Endpoint**: `/api/projects/search/visibility?visibility={visibility}`
- **Description**: Filters projects by visibility (`PUBLIC`, `PRIVATE`).
- **Auth Required**: Yes

---

### 3.7 Search Projects By Status
- **Method**: `GET`
- **Endpoint**: `/api/projects/search/status?status={status}`
- **Description**: Filters projects by status (e.g., `OPEN`, `IN_PROGRESS`, `COMPLETED`).
- **Auth Required**: Yes

---

### 3.8 Update Project
- **Method**: `PUT`
- **Endpoint**: `/api/projects/update/{id}`
- **Description**: Updates project fields including `name`, `type`, `description`, `visibility`, `latestUpdate`, `memberCount`, and `status`.
- **Auth Required**: Yes

#### Request:
```json
{
  "name": "ProjectMate Platform V2",
  "visibility": "PUBLIC",
  "latestUpdate": "Added Cloudinary image uploads and user profiles",
  "status": "IN_PROGRESS"
}
```

---

### 3.9 Delete Project
- **Method**: `DELETE`
- **Endpoint**: `/api/projects/delete/{id}`
- **Description**: Deletes a project by its ID.
- **Auth Required**: Yes

---

## 4. Project Member APIs

### 4.1 Add Member To Project
- **Method**: `POST`
- **Endpoint**: `/api/projects/{projectId}/members`
- **Description**: Adds a user to a project with a platform role (`MEMBER`, `ADMIN`) and an optional project role (`FRONTEND DEV`, `BACKEND DEV`, etc.).
- **Auth Required**: Yes

---

## 8. AI Assistance & Project Generation APIs

### 8.1 Analyze Project Idea / Prompt
- **Method**: `POST`
- **Endpoint**: `/api/ai/analyze`
- **Description**: Analyzes a user's project idea/prompt using Google AI Studio Gemini API (`gemini-2.5-flash`) and extracts structured project info, tech stack requirements with skill levels, and 4 team member roles with required skill proficiency scores (1-5).
- **Auth Required**: Yes

---

## 9. Notification Management APIs

### 9.1 Create Notification
- **Method**: `POST`
- **Endpoint**: `/api/notifications`
- **Description**: Creates a new user notification.
- **Auth Required**: Yes

---

## 10. Invitation Management APIs

### 10.1 Send Project Invitation
- **Method**: `POST`
- **Endpoint**: `/api/invitations`
- **Description**: Sends a project invitation from a sender user to a receiver user for a specific project. Automatically triggers a notification to the receiver.
- **Auth Required**: Yes

---

## 11. User Skills Profile APIs

### 11.1 Add or Update User Skill
- **Method**: `POST`
- **Endpoint**: `/api/user-skills`
- **Description**: Associates a skill with a user profile along with their proficiency level.
- **Auth Required**: Yes

---

## 12. Health Check APIs

### 12.1 Health Status Check
- **Method**: `GET`
- **Endpoint**: `/health` or `/api/health`
- **Description**: Simple ping endpoint to verify application operational health.
- **Auth Required**: No
