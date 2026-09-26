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
2. [Project Management APIs (`/api/projects`)](#2-project-management-apis)
3. [Project Member APIs (`/api/projects`)](#3-project-member-apis)
4. [Master Skill Catalog APIs (`/api/skills`)](#4-master-skill-catalog-apis)
5. [Project Tech Stack Requirements APIs (`/api/project-skills`)](#5-project-tech-stack-requirements-apis)
6. [Project Roles Catalog APIs (`/api/project-roles`)](#6-project-roles-catalog-apis)
7. [Health Check APIs (`/health`, `/api/health`)](#7-health-check-apis)

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

## 2. Project Management APIs

### 2.1 Create Project
- **Method**: `POST`
- **Endpoint**: `/api/projects/create`
- **Description**: Creates a new project and automatically assigns the user (`ownerId`) as a `ProjectMember` with role `OWNER`.
- **Auth Required**: Yes

#### Request:
```json
{
  "ownerId": 1,
  "name": "ProjectMate Platform",
  "type": "Web Application",
  "description": "Collaborative project matching for developers.",
  "memberCount": 5,
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
    "email": "sayan@example.com",
    "createdAt": "2026-09-26T10:30:00"
  },
  "name": "ProjectMate Platform",
  "type": "Web Application",
  "description": "Collaborative project matching for developers.",
  "memberCount": 5,
  "createdAt": "2026-09-26T11:00:00",
  "status": "OPEN"
}
```

---

### 2.2 Get Project By ID
- **Method**: `GET`
- **Endpoint**: `/api/projects/{id}`
- **Description**: Retrieves details for a specific project by its ID.
- **Auth Required**: Yes

#### Response (`200 OK`):
```json
{
  "id": 6,
  "owner": {
    "id": 1,
    "name": "Sayan Pal",
    "email": "sayan@example.com",
    "createdAt": "2026-09-26T10:30:00"
  },
  "name": "ProjectMate Platform",
  "type": "Web Application",
  "description": "Collaborative project matching for developers.",
  "memberCount": 5,
  "createdAt": "2026-09-26T11:00:00",
  "status": "OPEN"
}
```

---

### 2.3 List Projects By Owner ID
- **Method**: `GET`
- **Endpoint**: `/api/projects?ownerId={ownerId}`
- **Description**: Returns all projects created by a specific owner ID.
- **Auth Required**: Yes

#### Query Params:
- `ownerId` (Long): ID of the project owner.

#### Response (`200 OK`):
```json
[
  {
    "id": 6,
    "owner": {
      "id": 1,
      "name": "Sayan Pal",
      "email": "sayan@example.com",
      "createdAt": "2026-09-26T10:30:00"
    },
    "name": "ProjectMate Platform",
    "type": "Web Application",
    "description": "Collaborative project matching for developers.",
    "memberCount": 5,
    "createdAt": "2026-09-26T11:00:00",
    "status": "OPEN"
  }
]
```

---

### 2.4 Search Projects By Name
- **Method**: `GET`
- **Endpoint**: `/api/projects/search?name={name}`
- **Description**: Searches projects by a substring matching their name (case-insensitive).
- **Auth Required**: Yes

#### Response (`200 OK`):
```json
[
  {
    "id": 6,
    "name": "ProjectMate Platform",
    "type": "Web Application",
    "description": "Collaborative project matching for developers.",
    "memberCount": 5,
    "status": "OPEN"
  }
]
```

---

### 2.5 Search Projects By Type
- **Method**: `GET`
- **Endpoint**: `/api/projects/search/type?type={type}`
- **Description**: Filters projects by category/type (e.g., `Web Application`, `Mobile`, `AI`).
- **Auth Required**: Yes

#### Response (`200 OK`):
```json
[
  {
    "id": 6,
    "name": "ProjectMate Platform",
    "type": "Web Application"
  }
]
```

---

### 2.6 Search Projects By Status
- **Method**: `GET`
- **Endpoint**: `/api/projects/search/status?status={status}`
- **Description**: Filters projects by status (e.g., `OPEN`, `IN_PROGRESS`, `COMPLETED`).
- **Auth Required**: Yes

#### Response (`200 OK`):
```json
[
  {
    "id": 6,
    "name": "ProjectMate Platform",
    "status": "OPEN"
  }
]
```

---

### 2.7 Search Projects By Owner and Status
- **Method**: `GET`
- **Endpoint**: `/api/projects/search/owner-status?ownerId={ownerId}&status={status}`
- **Description**: Filters projects belonging to a specific owner with a specific status.
- **Auth Required**: Yes

---

### 2.8 Update Project
- **Method**: `PUT`
- **Endpoint**: `/api/projects/update/{id}`
- **Description**: Updates project fields for an existing project.
- **Auth Required**: Yes

#### Request:
```json
{
  "name": "ProjectMate Platform V2",
  "type": "Full Stack",
  "description": "Updated project description",
  "memberCount": 6,
  "status": "IN_PROGRESS"
}
```

#### Response (`200 OK`):
```json
{
  "id": 6,
  "name": "ProjectMate Platform V2",
  "type": "Full Stack",
  "status": "IN_PROGRESS"
}
```

---

### 2.9 Delete Project
- **Method**: `DELETE`
- **Endpoint**: `/api/projects/delete/{id}`
- **Description**: Deletes a project by its ID.
- **Auth Required**: Yes

#### Response (`204 No Content`): Empty response body.

---

## 3. Project Member APIs

### 3.1 Add Member To Project
- **Method**: `POST`
- **Endpoint**: `/api/projects/{projectId}/members`
- **Description**: Adds a user to a project with a platform role (`MEMBER`, `ADMIN`) and an optional project role (`FRONTEND DEV`, `BACKEND DEV`, etc.).
- **Auth Required**: Yes

#### Query Params:
- `userId` (Long, required): ID of user being added.
- `role` (String, optional, default: `"MEMBER"`): Platform membership role (`OWNER`, `MEMBER`, `ADMIN`).
- `roleInProject` (String, optional, default: `null`): Specific role title (e.g., `"FRONTEND DEV"`).

#### Example URL:
`POST /api/projects/6/members?userId=2&role=MEMBER&roleInProject=FRONTEND%20DEV`

#### Response (`200 OK`):
```json
{
  "id": 12,
  "projectId": 6,
  "user": {
    "id": 2,
    "name": "Alice Smith",
    "email": "alice@example.com",
    "createdAt": "2026-09-26T10:35:00"
  },
  "role": "MEMBER",
  "roleInProject": "FRONTEND DEV",
  "assignedAt": "2026-09-26T11:15:00"
}
```

---

### 3.2 List All Project Members
- **Method**: `GET`
- **Endpoint**: `/api/projects/{projectId}/members`
- **Description**: Returns all members belonging to a project.
- **Auth Required**: Yes

#### Response (`200 OK`):
```json
[
  {
    "id": 1,
    "projectId": 6,
    "user": {
      "id": 1,
      "name": "Sayan Pal",
      "email": "sayan@example.com"
    },
    "role": "OWNER",
    "roleInProject": null,
    "assignedAt": "2026-09-26T11:00:00"
  },
  {
    "id": 12,
    "projectId": 6,
    "user": {
      "id": 2,
      "name": "Alice Smith",
      "email": "alice@example.com"
    },
    "role": "MEMBER",
    "roleInProject": "FRONTEND DEV",
    "assignedAt": "2026-09-26T11:15:00"
  }
]
```

---

### 3.3 Check Membership Status
- **Method**: `GET`
- **Endpoint**: `/api/projects/{projectId}/members/check?userId={userId}`
- **Description**: Checks whether a user is a member of the project.
- **Auth Required**: Yes

#### Response (`200 OK`):
```json
true
```

---

### 3.4 Check Owner Status
- **Method**: `GET`
- **Endpoint**: `/api/projects/{projectId}/owner/check?userId={userId}`
- **Description**: Checks if a user is the owner of a project.
- **Auth Required**: Yes

#### Response (`200 OK`):
```json
true
```

---

### 3.5 Update Member Platform Role
- **Method**: `PUT`
- **Endpoint**: `/api/projects/{projectId}/members/{userId}/role?role={role}`
- **Description**: Updates the membership role (e.g. from `MEMBER` to `ADMIN`).
- **Auth Required**: Yes

#### Response (`200 OK`):
```json
{
  "id": 12,
  "projectId": 6,
  "user": { "id": 2, "name": "Alice Smith" },
  "role": "ADMIN",
  "roleInProject": "FRONTEND DEV"
}
```

---

### 3.6 Update Member Title in Project
- **Method**: `PUT`
- **Endpoint**: `/api/projects/{projectId}/members/{userId}/role-in-project?roleInProject={roleInProject}`
- **Description**: Updates a member's specific title/role in the project (e.g. `UI/UX DESIGNER`).
- **Auth Required**: Yes

#### Response (`200 OK`):
```json
{
  "id": 12,
  "projectId": 6,
  "user": { "id": 2, "name": "Alice Smith" },
  "role": "MEMBER",
  "roleInProject": "UI/UX DESIGNER"
}
```

---

### 3.7 Remove Member From Project
- **Method**: `DELETE`
- **Endpoint**: `/api/projects/{projectId}/members/{userId}`
- **Description**: Removes a member from a project.
- **Auth Required**: Yes

#### Response (`204 No Content`): Empty response body.

---

### 3.8 List All Projects for a User
- **Method**: `GET`
- **Endpoint**: `/api/projects/members/user/{userId}`
- **Description**: Retrieves all project memberships for a given user ID.
- **Auth Required**: Yes

---

## 4. Master Skill Catalog APIs

### 4.1 Create Skill
- **Method**: `POST`
- **Endpoint**: `/api/skills`
- **Description**: Adds a new skill to the global master skill catalog.
- **Auth Required**: Yes

#### Request:
```json
{
  "name": "Java"
}
```

#### Response (`200 OK`):
```json
{
  "id": 1,
  "name": "Java"
}
```

---

### 4.2 Get All Skills
- **Method**: `GET`
- **Endpoint**: `/api/skills`
- **Description**: Returns all skills available in the master catalog.
- **Auth Required**: Yes

#### Response (`200 OK`):
```json
[
  { "id": 1, "name": "Java" },
  { "id": 2, "name": "React" },
  { "id": 3, "name": "Docker" }
]
```

---

### 4.3 Search Skill By Name
- **Method**: `GET`
- **Endpoint**: `/api/skills/search?name={name}`
- **Description**: Searches a skill by name (case-insensitive).
- **Auth Required**: Yes

---

### 4.4 Update Skill
- **Method**: `PUT`
- **Endpoint**: `/api/skills/{id}`
- **Description**: Updates the name of an existing skill.
- **Auth Required**: Yes

#### Request:
```json
{
  "name": "Java 21"
}
```

---

### 4.5 Delete Skill
- **Method**: `DELETE`
- **Endpoint**: `/api/skills/{id}`
- **Description**: Deletes a skill from the catalog.
- **Auth Required**: Yes

#### Response (`204 No Content`): Empty response body.

---

## 5. Project Tech Stack Requirements APIs

### 5.1 Add Required Skill to Project
- **Method**: `POST`
- **Endpoint**: `/api/project-skills`
- **Description**: Associates a required skill with a project along with the target proficiency level (`Beginner`, `Intermediate`, `Expert`).
- **Auth Required**: Yes

#### Request:
```json
{
  "projectId": 6,
  "skillId": 1,
  "level": "Expert"
}
```

#### Response (`200 OK`):
```json
{
  "id": 3,
  "projectId": 6,
  "skill": {
    "id": 1,
    "name": "Java"
  },
  "level": "Expert"
}
```

---

### 5.2 List All Required Skills for a Project
- **Method**: `GET`
- **Endpoint**: `/api/project-skills/project/{projectId}`
- **Description**: Returns all tech stack requirements for a specified project ID.
- **Auth Required**: Yes

#### Response (`200 OK`):
```json
[
  {
    "id": 3,
    "projectId": 6,
    "skill": { "id": 1, "name": "Java" },
    "level": "Expert"
  },
  {
    "id": 4,
    "projectId": 6,
    "skill": { "id": 2, "name": "React" },
    "level": "Intermediate"
  }
]
```

---

### 5.3 Filter Project Requirements By Skill Level
- **Method**: `GET`
- **Endpoint**: `/api/project-skills/project/{projectId}/level/{level}`
- **Description**: Returns skills required for a project matching a specific level.
- **Auth Required**: Yes

---

### 5.4 Update Requirement Level
- **Method**: `PUT`
- **Endpoint**: `/api/project-skills/{id}?level={level}`
- **Description**: Updates the proficiency level required for a project skill.
- **Auth Required**: Yes

---

### 5.5 Remove Skill Requirement
- **Method**: `DELETE`
- **Endpoint**: `/api/project-skills/{id}`
- **Description**: Removes a project skill requirement by ID.
- **Auth Required**: Yes

---

## 6. Project Roles Catalog APIs

### 6.1 Create Project Role Title
- **Method**: `POST`
- **Endpoint**: `/api/project-roles`
- **Description**: Adds a new standardized project role title to the master catalog.
- **Auth Required**: Yes

#### Request:
```json
{
  "name": "Frontend Engineer"
}
```

#### Response (`200 OK`):
```json
{
  "id": 1,
  "name": "Frontend Engineer"
}
```

---

### 6.2 Get All Project Roles
- **Method**: `GET`
- **Endpoint**: `/api/project-roles`
- **Description**: Lists all standardized project role titles.
- **Auth Required**: Yes

#### Response (`200 OK`):
```json
[
  { "id": 1, "name": "Frontend Engineer" },
  { "id": 2, "name": "DevOps Specialist" },
  { "id": 3, "name": "UI/UX Designer" }
]
```

---

### 6.3 Search Role By Name
- **Method**: `GET`
- **Endpoint**: `/api/project-roles/search?name={name}`
- **Description**: Searches a project role title by name (case-insensitive).
- **Auth Required**: Yes

---

### 6.4 Delete Project Role
- **Method**: `DELETE`
- **Endpoint**: `/api/project-roles/{id}`
- **Description**: Deletes a role title from the catalog.
- **Auth Required**: Yes

#### Response (`204 No Content`): Empty response body.

---

## 7. Health Check APIs

### 7.1 Health Status Check
- **Method**: `GET`
- **Endpoint**: `/health` or `/api/health`
- **Description**: Simple ping endpoint to verify application operational health.
- **Auth Required**: No

#### Response (`200 OK`):
```text
OK
```
