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

#### Response (`200 OK`):
```json
{
  "id": 1,
  "name": "Sayan Pal",
  "email": "sayan@example.com",
  "createdAt": "2026-09-26T10:30:00"
}
```

---

### 2.3 Create User
- **Method**: `POST`
- **Endpoint**: `/api/users`
- **Description**: Creates a new user record.
- **Auth Required**: Yes

#### Request:
```json
{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "password": "Password@123"
}
```

#### Response (`201 Created`):
```json
{
  "id": 2,
  "name": "Jane Doe",
  "email": "jane@example.com",
  "createdAt": "2026-09-29T08:00:00"
}
```

---

### 2.4 Update User
- **Method**: `PUT`
- **Endpoint**: `/api/users/{id}`
- **Description**: Updates user profile details such as name, email, or password.
- **Auth Required**: Yes

#### Request:
```json
{
  "name": "Jane Doe Updated",
  "email": "jane.updated@example.com",
  "password": "NewPassword@123"
}
```

#### Response (`200 OK`):
```json
{
  "id": 2,
  "name": "Jane Doe Updated",
  "email": "jane.updated@example.com",
  "createdAt": "2026-09-29T08:00:00"
}
```

---

### 2.5 Delete User
- **Method**: `DELETE`
- **Endpoint**: `/api/users/{id}`
- **Description**: Deletes a user by their ID.
- **Auth Required**: Yes

#### Response (`204 No Content`): Empty response body.

---

### 2.6 Search Users By Name
- **Method**: `GET`
- **Endpoint**: `/api/users/search?name={name}`
- **Description**: Searches users matching a name substring (case-insensitive).
- **Auth Required**: Yes

#### Response (`200 OK`):
```json
[
  {
    "id": 1,
    "name": "Sayan Pal",
    "email": "sayan@example.com",
    "createdAt": "2026-09-26T10:30:00"
  }
]
```

---

### 2.7 Get User By Email
- **Method**: `GET`
- **Endpoint**: `/api/users/search/email?email={email}`
- **Description**: Retrieves a user matching the specified email address.
- **Auth Required**: Yes

#### Response (`200 OK`):
```json
{
  "id": 1,
  "name": "Sayan Pal",
  "email": "sayan@example.com",
  "createdAt": "2026-09-26T10:30:00"
}
```

---

### 2.8 Get Users By Role
- **Method**: `GET`
- **Endpoint**: `/api/users/search/role?role={role}`
- **Description**: Returns all users matching a specified role.
- **Auth Required**: Yes

---

### 2.9 Get Users By Role IN List
- **Method**: `GET`
- **Endpoint**: `/api/users/search/role/in?roles={roles}`
- **Description**: Filters users whose role is contained in the provided list of roles.
- **Auth Required**: Yes

---

### 2.10 Get Users By Role NOT IN List
- **Method**: `GET`
- **Endpoint**: `/api/users/search/role/not-in?roles={roles}`
- **Description**: Filters users whose role is not in the provided list of roles.
- **Auth Required**: Yes

---

## 3. Project Management APIs

### 3.1 Create Project
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

### 3.2 Get Project By ID
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

### 3.3 List Projects By Owner ID
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

### 3.4 Search Projects By Name
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

### 3.5 Search Projects By Type
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

### 3.6 Search Projects By Status
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

### 3.7 Search Projects By Owner and Status
- **Method**: `GET`
- **Endpoint**: `/api/projects/search/owner-status?ownerId={ownerId}&status={status}`
- **Description**: Filters projects belonging to a specific owner with a specific status.
- **Auth Required**: Yes

---

### 3.8 Search Project By Exact Name
- **Method**: `GET`
- **Endpoint**: `/api/projects/search/exact-name?name={name}`
- **Description**: Retrieves a project matching an exact project name.
- **Auth Required**: Yes

---

### 3.9 Update Project
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

### 3.10 Delete Project
- **Method**: `DELETE`
- **Endpoint**: `/api/projects/delete/{id}`
- **Description**: Deletes a project by its ID.
- **Auth Required**: Yes

#### Response (`204 No Content`): Empty response body.

---

## 4. Project Member APIs

### 4.1 Add Member To Project
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

### 4.2 List All Project Members
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

### 4.3 Check Membership Status
- **Method**: `GET`
- **Endpoint**: `/api/projects/{projectId}/members/check?userId={userId}`
- **Description**: Checks whether a user is a member of the project.
- **Auth Required**: Yes

#### Response (`200 OK`):
```json
true
```

---

### 4.4 Check Owner Status
- **Method**: `GET`
- **Endpoint**: `/api/projects/{projectId}/owner/check?userId={userId}`
- **Description**: Checks if a user is the owner of a project.
- **Auth Required**: Yes

#### Response (`200 OK`):
```json
true
```

---

### 4.5 Update Member Platform Role
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

### 4.6 Update Member Title in Project
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

### 4.7 Remove Member From Project
- **Method**: `DELETE`
- **Endpoint**: `/api/projects/{projectId}/members/{userId}`
- **Description**: Removes a member from a project.
- **Auth Required**: Yes

#### Response (`204 No Content`): Empty response body.

---

### 4.8 List All Projects for a User
- **Method**: `GET`
- **Endpoint**: `/api/projects/members/user/{userId}`
- **Description**: Retrieves all project memberships for a given user ID.
- **Auth Required**: Yes

---

## 5. Master Skill Catalog APIs

### 5.1 Create Skill
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

### 5.2 Get All Skills
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

### 5.3 Search Skill By Name
- **Method**: `GET`
- **Endpoint**: `/api/skills/search?name={name}`
- **Description**: Searches a skill by name (case-insensitive).
- **Auth Required**: Yes

---

### 5.4 Update Skill
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

### 5.5 Delete Skill
- **Method**: `DELETE`
- **Endpoint**: `/api/skills/{id}`
- **Description**: Deletes a skill from the catalog.
- **Auth Required**: Yes

#### Response (`204 No Content`): Empty response body.

---

## 6. Project Tech Stack Requirements APIs

### 6.1 Add Required Skill to Project
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

### 6.2 List All Required Skills for a Project
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

### 6.3 Filter Project Requirements By Skill Level
- **Method**: `GET`
- **Endpoint**: `/api/project-skills/project/{projectId}/level/{level}`
- **Description**: Returns skills required for a project matching a specific level.
- **Auth Required**: Yes

---

### 6.4 Update Requirement Level
- **Method**: `PUT`
- **Endpoint**: `/api/project-skills/{id}?level={level}`
- **Description**: Updates the proficiency level required for a project skill.
- **Auth Required**: Yes

---

### 6.5 Remove Skill Requirement
- **Method**: `DELETE`
- **Endpoint**: `/api/project-skills/{id}`
- **Description**: Removes a project skill requirement by ID.
- **Auth Required**: Yes

---

## 7. Project Roles Catalog APIs

### 7.1 Create Project Role Title
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

### 7.2 Get All Project Roles
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

### 7.3 Search Role By Name
- **Method**: `GET`
- **Endpoint**: `/api/project-roles/search?name={name}`
- **Description**: Searches a project role title by name (case-insensitive).
- **Auth Required**: Yes

---

### 7.4 Delete Project Role
- **Method**: `DELETE`
- **Endpoint**: `/api/project-roles/{id}`
- **Description**: Deletes a role title from the catalog.
- **Auth Required**: Yes

#### Response (`204 No Content`): Empty response body.

---

## 8. AI Assistance & Project Generation APIs

### 8.1 Analyze Project Idea / Prompt
- **Method**: `POST`
- **Endpoint**: `/api/ai/analyze`
- **Description**: Analyzes a user's project idea/prompt using Google AI Studio Gemini API (`gemini-2.5-flash`) and extracts structured project info, tech stack requirements with skill levels, and 4 team member roles with required skill proficiency scores (1-5). Returns a fallback response if the AI service API key is unconfigured or unavailable.
- **Auth Required**: Yes

#### Request:
```json
{
  "prompt": "A real-time workspace for remote design teams featuring live canvas sharing and AI asset generation."
}
```

#### Response (`200 OK`):
```json
{
  "name": "DesignSync AI",
  "type": "Full Stack",
  "description": "A real-time workspace for remote design teams featuring live canvas sharing and AI asset generation.",
  "memberCount": 4,
  "status": "OPEN",
  "projectSkills": [
    {
      "skillName": "Next.js",
      "level": "Expert"
    },
    {
      "skillName": "WebSockets",
      "level": "Intermediate"
    },
    {
      "skillName": "PostgreSQL",
      "level": "Intermediate"
    }
  ],
  "roles": [
    {
      "roleName": "Frontend Lead",
      "skills": [
        {
          "skillName": "Next.js",
          "proficiencyRequired": 5
        },
        {
          "skillName": "TypeScript",
          "proficiencyRequired": 4
        }
      ]
    },
    {
      "roleName": "Backend Engineer",
      "skills": [
        {
          "skillName": "WebSockets",
          "proficiencyRequired": 4
        },
        {
          "skillName": "PostgreSQL",
          "proficiencyRequired": 3
        }
      ]
    },
    {
      "roleName": "DevOps Specialist",
      "skills": [
        {
          "skillName": "Docker",
          "proficiencyRequired": 4
        }
      ]
    },
    {
      "roleName": "UI/UX Designer",
      "skills": [
        {
          "skillName": "Figma",
          "proficiencyRequired": 4
        }
      ]
    }
  ]
}
```

---

### 8.2 Create Project From AI Prompt
- **Method**: `POST`
- **Endpoint**: `/api/ai/create-project?ownerId={ownerId}`
- **Description**: Analyzes a user's prompt using Gemini API and automatically creates and persists the `Project`, required `Skills`, and 4 `Project Roles` in the database, assigning the specified user (`ownerId`) as `OWNER`.
- **Auth Required**: Yes

#### Query Params:
- `ownerId` (Long, required): ID of the user creating the project.

#### Request:
```json
{
  "prompt": "A real-time workspace for remote design teams featuring live canvas sharing and AI asset generation."
}
```

#### Response (`200 OK`):
```json
{
  "id": 10,
  "owner": {
    "id": 1,
    "name": "Sayan Pal",
    "email": "sayan@example.com",
    "createdAt": "2026-09-26T10:30:00"
  },
  "name": "DesignSync AI",
  "type": "Full Stack",
  "description": "A real-time workspace for remote design teams featuring live canvas sharing and AI asset generation.",
  "memberCount": 4,
  "createdAt": "2026-09-29T12:00:00",
  "status": "OPEN"
}
```

---

### 8.3 Save AI Analysis as Project
- **Method**: `POST`
- **Endpoint**: `/api/ai/save-analysis?ownerId={ownerId}`
- **Description**: Persists an existing AI project analysis object (`AiProjectAnalysisResponse`) directly into the database as a new project with its skills and role requirements.
- **Auth Required**: Yes

#### Query Params:
- `ownerId` (Long, required): ID of the user creating the project.

---

## 9. Notification Management APIs

### 9.1 Create Notification
- **Method**: `POST`
- **Endpoint**: `/api/notifications`
- **Description**: Creates a new user notification.
- **Auth Required**: Yes

#### Request:
```json
{
  "userId": 1,
  "message": "Welcome to ProjectMate! Explore open projects and match your skills.",
  "expiresAt": "2026-10-30T00:00:00"
}
```

#### Response (`201 Created`):
```json
{
  "id": 1,
  "userId": 1,
  "userName": "Sayan Pal",
  "message": "Welcome to ProjectMate! Explore open projects and match your skills.",
  "isRead": false,
  "createdAt": "2026-10-02T08:30:00",
  "expiresAt": "2026-10-30T00:00:00",
  "readAt": null,
  "deletedAt": null
}
```

---

### 9.2 Get All Notifications for User
- **Method**: `GET`
- **Endpoint**: `/api/notifications/user/{userId}`
- **Description**: Retrieves all non-deleted notifications for a specified user ID.
- **Auth Required**: Yes

---

### 9.3 Get Unread Notifications for User
- **Method**: `GET`
- **Endpoint**: `/api/notifications/user/{userId}/unread`
- **Description**: Retrieves all unread, non-deleted notifications for a specified user ID.
- **Auth Required**: Yes

---

### 9.4 Get Unread Notification Count
- **Method**: `GET`
- **Endpoint**: `/api/notifications/user/{userId}/unread-count`
- **Description**: Returns the integer count of unread notifications for a specified user ID.
- **Auth Required**: Yes

#### Response (`200 OK`):
```json
3
```

---

### 9.5 Mark Notification as Read
- **Method**: `PUT`
- **Endpoint**: `/api/notifications/{id}/read?userId={userId}`
- **Description**: Marks a specific notification as read.
- **Auth Required**: Yes

---

### 9.6 Mark All Notifications as Read
- **Method**: `PUT`
- **Endpoint**: `/api/notifications/user/{userId}/read-all`
- **Description**: Marks all unread notifications for a user as read.
- **Auth Required**: Yes

---

### 9.7 Soft Delete Notification
- **Method**: `DELETE`
- **Endpoint**: `/api/notifications/{id}?userId={userId}`
- **Description**: Soft deletes a notification for a user.
- **Auth Required**: Yes

#### Response (`204 No Content`): Empty response body.

---

## 10. Invitation Management APIs

### 10.1 Send Project Invitation
- **Method**: `POST`
- **Endpoint**: `/api/invitations`
- **Description**: Sends a project invitation from a sender user to a receiver user for a specific project. Automatically triggers a notification to the receiver.
- **Auth Required**: Yes

#### Request:
```json
{
  "senderId": 1,
  "receiverId": 2,
  "projectId": 6,
  "expiresAt": "2026-10-15T00:00:00"
}
```

#### Response (`201 Created`):
```json
{
  "id": 1,
  "sender": {
    "id": 1,
    "name": "Sayan Pal",
    "email": "sayan@example.com"
  },
  "receiver": {
    "id": 2,
    "name": "Alice Smith",
    "email": "alice@example.com"
  },
  "project": {
    "id": 6,
    "name": "ProjectMate Platform"
  },
  "status": "PENDING",
  "createdAt": "2026-10-02T08:30:00",
  "expiresAt": "2026-10-15T00:00:00",
  "acceptedAt": null,
  "rejectedAt": null
}
```

---

### 10.2 Get Received Invitations
- **Method**: `GET`
- **Endpoint**: `/api/invitations/receiver/{receiverId}`
- **Description**: Returns all project invitations received by a user.
- **Auth Required**: Yes

---

### 10.3 Get Pending Received Invitations
- **Method**: `GET`
- **Endpoint**: `/api/invitations/receiver/{receiverId}/pending`
- **Description**: Returns pending invitations received by a user.
- **Auth Required**: Yes

---

### 10.4 Get Sent Invitations
- **Method**: `GET`
- **Endpoint**: `/api/invitations/sender/{senderId}`
- **Description**: Returns all project invitations sent by a user.
- **Auth Required**: Yes

---

### 10.5 Get Invitations for a Project
- **Method**: `GET`
- **Endpoint**: `/api/invitations/project/{projectId}`
- **Description**: Returns all invitations associated with a specific project ID.
- **Auth Required**: Yes

---

### 10.6 Accept Project Invitation
- **Method**: `PUT`
- **Endpoint**: `/api/invitations/{id}/accept?receiverId={receiverId}`
- **Description**: Accepts a project invitation, automatically adds the user as a `ProjectMember` with role `MEMBER`, and notifies the sender.
- **Auth Required**: Yes

#### Response (`200 OK`):
```json
{
  "id": 1,
  "status": "ACCEPTED",
  "acceptedAt": "2026-10-02T08:35:00"
}
```

---

### 10.7 Reject Project Invitation
- **Method**: `PUT`
- **Endpoint**: `/api/invitations/{id}/reject?receiverId={receiverId}`
- **Description**: Declines a project invitation and notifies the sender.
- **Auth Required**: Yes

---

### 10.8 Cancel Project Invitation
- **Method**: `PUT`
- **Endpoint**: `/api/invitations/{id}/cancel?senderId={senderId}`
- **Description**: Cancels a pending project invitation sent by the sender.
- **Auth Required**: Yes

---

## 11. User Skills Profile APIs

### 11.1 Add or Update User Skill
- **Method**: `POST`
- **Endpoint**: `/api/user-skills`
- **Description**: Associates a skill with a user profile along with their proficiency level. Accepts either `skillId` or `skillName` (auto-creates skill if new). If already associated, updates proficiency.
- **Auth Required**: Yes

#### Request:
```json
{
  "userId": 1,
  "skillName": "Java",
  "proficiency": 5
}
```

#### Response (`201 Created`):
```json
{
  "id": 1,
  "userId": 1,
  "skill": {
    "id": 1,
    "name": "Java"
  },
  "proficiency": 5
}
```

---

### 11.2 Get User Skill by ID
- **Method**: `GET`
- **Endpoint**: `/api/user-skills/{id}`
- **Description**: Retrieves details for a specific user skill association by ID.
- **Auth Required**: Yes

---

### 11.3 Get All Skills for a User
- **Method**: `GET`
- **Endpoint**: `/api/user-skills/user/{userId}`
- **Description**: Returns all skills possessed by a specific user.
- **Auth Required**: Yes

#### Response (`200 OK`):
```json
[
  {
    "id": 1,
    "userId": 1,
    "skill": { "id": 1, "name": "Java" },
    "proficiency": 5
  },
  {
    "id": 2,
    "userId": 1,
    "skill": { "id": 2, "name": "Spring Boot" },
    "proficiency": 4
  }
]
```

---

### 11.4 Get Users Having a Skill
- **Method**: `GET`
- **Endpoint**: `/api/user-skills/skill/{skillId}`
- **Description**: Returns all user skill profiles possessing a specified skill ID.
- **Auth Required**: Yes

---

### 11.5 Filter Users by Skill and Minimum Proficiency
- **Method**: `GET`
- **Endpoint**: `/api/user-skills/skill/{skillId}/min-proficiency/{minProficiency}`
- **Description**: Filters users who possess a skill with at least the specified minimum proficiency rating.
- **Auth Required**: Yes

---

### 11.6 Update User Skill Proficiency
- **Method**: `PUT`
- **Endpoint**: `/api/user-skills/{id}?proficiency={proficiency}`
- **Description**: Updates the proficiency level for an existing user skill.
- **Auth Required**: Yes

---

### 11.7 Delete User Skill
- **Method**: `DELETE`
- **Endpoint**: `/api/user-skills/{id}`
- **Description**: Removes a skill from a user profile by ID.
- **Auth Required**: Yes

#### Response (`204 No Content`): Empty response body.

---

### 11.8 Remove Skill from User Profile
- **Method**: `DELETE`
- **Endpoint**: `/api/user-skills/user/{userId}/skill/{skillId}`
- **Description**: Removes a skill from a user profile using user ID and skill ID.
- **Auth Required**: Yes

---

## 12. Health Check APIs

### 12.1 Health Status Check
- **Method**: `GET`
- **Endpoint**: `/health` or `/api/health`
- **Description**: Simple ping endpoint to verify application operational health.
- **Auth Required**: No

#### Response (`200 OK`):
```text
OK
```
