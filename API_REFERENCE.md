# 📖 ProjectMate Production API Reference

Welcome to the official **ProjectMate REST API Reference**. This production-grade specification details every active endpoint, query parameter, HTTP verb, request payload schema, and response object for the ProjectMate backend platform.

---

## 🌐 General Configuration & Conventions

- **Base URL**: `http://localhost:8080` (Development) / `https://<your-render-app>.onrender.com` (Production)
- **Content-Type**: `application/json`
- **Authentication**: Stateless Bearer Token via standard HTTP Header:
  ```http
  Authorization: Bearer <jwt_access_token>
  ```
- **Error Response Standard**:
  ```json
  {
    "status": 401,
    "error": "Unauthorized",
    "message": "Authentication token is missing or invalid."
  }
  ```

---

## 📑 Complete Table of Contents

1. [Authentication APIs (`/api/auth/v1`)](#1-authentication-apis)
2. [User Management APIs (`/api/users`)](#2-user-management-apis)
3. [Project Management APIs (`/api/projects`)](#3-project-management-apis)
4. [Project Member Operations APIs (`/api/projects`)](#4-project-member-operations-apis)
5. [Master Skill Catalog APIs (`/api/skills`)](#5-master-skill-catalog-apis)
6. [Project Tech Stack Requirements APIs (`/api/project-skills`)](#6-project-tech-stack-requirements-apis)
7. [Project Roles Catalog APIs (`/api/project-roles`)](#7-project-roles-catalog-apis)
8. [AI Assistance & Project Generation APIs (`/api/ai`)](#8-ai-assistance--project-generation-apis)
9. [Notification Management APIs (`/api/notifications`)](#9-notification-management-apis)
10. [Invitation Management APIs (`/api/invitations`)](#10-invitation-management-apis)
11. [User Skills Profile APIs (`/api/user-skills`)](#11-user-skills-profile-apis)
12. [Automated Team Referral & Matchmaking APIs (`/api/refer`)](#12-automated-team-referral--matchmaking-apis)
13. [Health Check APIs (`/api/health`)](#13-health-check-apis)

---

## 1. Authentication APIs

### 1.1 Register User Account
- **Method**: `POST`
- **Endpoint**: `/api/auth/v1/register`
- **Description**: Registers a new user account, encodes password with BCrypt, sets default role (`USER`), and returns JWT access & refresh tokens.
- **Auth Required**: No

#### Request Body:
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
    "role": "USER",
    "about": null,
    "profilePic": null,
    "githubLink": null,
    "linkedinLink": null,
    "createdAt": "2026-09-26T10:30:00"
  }
}
```

---

### 1.2 Login User
- **Method**: `POST`
- **Endpoint**: `/api/auth/v1/login`
- **Description**: Validates user credentials and issues a fresh JWT access token along with a refresh token.
- **Auth Required**: No

#### Request Body:
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
    "role": "USER",
    "about": "Full Stack Architect",
    "profilePic": "https://res.cloudinary.com/demo/image/upload/v1234567/sample.jpg",
    "githubLink": "https://github.com/reddevil212",
    "linkedinLink": "https://linkedin.com/in/sayanpal",
    "createdAt": "2026-09-26T10:30:00"
  }
}
```

---

### 1.3 Refresh Access Token
- **Method**: `POST`
- **Endpoint**: `/api/auth/v1/refresh`
- **Description**: Exchanges a valid refresh token for a newly generated JWT access token.
- **Auth Required**: No

#### Request Body:
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
    "role": "USER"
  }
}
```

---

### 1.4 Logout User
- **Method**: `POST`
- **Endpoint**: `/api/auth/v1/logout`
- **Description**: Revokes and invalidates active refresh tokens for the given user ID.
- **Auth Required**: Yes

#### Request Body:
```json
1
```

#### Response (`200 OK`): Empty response body.

---

## 2. User Management APIs

### 2.1 Get All Users
- **Method**: `GET`
- **Endpoint**: `/api/users`
- **Description**: Returns all registered platform users.
- **Auth Required**: Yes

#### Response (`200 OK`):
```json
[
  {
    "id": 1,
    "name": "Sayan Pal",
    "email": "sayan@example.com",
    "role": "USER",
    "about": "Full stack developer",
    "profilePic": "https://example.com/profile.jpg",
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
- **Description**: Retrieves full profile details for a specific user ID.
- **Auth Required**: Yes

#### Response (`200 OK`):
```json
{
  "id": 1,
  "name": "Sayan Pal",
  "email": "sayan@example.com",
  "role": "USER",
  "about": "Full Stack Architect",
  "profilePic": "https://example.com/profile.jpg",
  "githubLink": "https://github.com/reddevil212",
  "linkedinLink": "https://linkedin.com/in/sayanpal",
  "createdAt": "2026-09-26T10:30:00"
}
```

---

### 2.3 Create User
- **Method**: `POST`
- **Endpoint**: `/api/users`
- **Description**: Admin endpoint to create a user record manually.
- **Auth Required**: Yes

#### Request Body:
```json
{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "password": "Password@123",
  "role": "USER",
  "about": "Frontend Specialist",
  "profilePic": "https://example.com/jane.jpg",
  "githubLink": "https://github.com/janedoe",
  "linkedinLink": "https://linkedin.com/in/janedoe"
}
```

#### Response (`201 Created`):
```json
{
  "id": 2,
  "name": "Jane Doe",
  "email": "jane@example.com",
  "role": "USER",
  "about": "Frontend Specialist",
  "profilePic": "https://example.com/jane.jpg",
  "githubLink": "https://github.com/janedoe",
  "linkedinLink": "https://linkedin.com/in/janedoe",
  "createdAt": "2026-09-29T08:00:00"
}
```

---

### 2.4 Update User Profile
- **Method**: `PUT`
- **Endpoint**: `/api/users/{id}`
- **Description**: Updates user profile attributes including `name`, `email`, `about`, `profilePic` (client-generated image CDN URL), `githubLink`, `linkedinLink`, and `role`.
- **Auth Required**: Yes

#### Request Body:
```json
{
  "name": "Jane Doe Updated",
  "about": "Senior Full Stack Lead",
  "profilePic": "https://res.cloudinary.com/demo/image/upload/v1234567/profile.jpg",
  "githubLink": "https://github.com/janedoe-updated",
  "linkedinLink": "https://linkedin.com/in/janedoe-updated"
}
```

#### Response (`200 OK`):
```json
{
  "id": 2,
  "name": "Jane Doe Updated",
  "email": "jane@example.com",
  "role": "USER",
  "about": "Senior Full Stack Lead",
  "profilePic": "https://res.cloudinary.com/demo/image/upload/v1234567/profile.jpg",
  "githubLink": "https://github.com/janedoe-updated",
  "linkedinLink": "https://linkedin.com/in/janedoe-updated",
  "createdAt": "2026-09-29T08:00:00"
}
```

---

### 2.5 Delete User
- **Method**: `DELETE`
- **Endpoint**: `/api/users/{id}`
- **Description**: Permanently deletes user account by ID.
- **Auth Required**: Yes

#### Response (`204 No Content`): Empty response body.

---

### 2.6 Search Users By Name Substring
- **Method**: `GET`
- **Endpoint**: `/api/users/search?name={name}`
- **Description**: Case-insensitive substring search across user names.
- **Auth Required**: Yes

---

### 2.7 Get User By Email
- **Method**: `GET`
- **Endpoint**: `/api/users/search/email?email={email}`
- **Description**: Exact match lookup for user by email address.
- **Auth Required**: Yes

---

### 2.8 Get Users By Role
- **Method**: `GET`
- **Endpoint**: `/api/users/search/role?role={role}`
- **Auth Required**: Yes

---

### 2.9 Get Users By Role IN List
- **Method**: `GET`
- **Endpoint**: `/api/users/search/role/in?roles={roles}`
- **Example URL**: `/api/users/search/role/in?roles=USER&roles=ADMIN`
- **Auth Required**: Yes

---

### 2.10 Get Users By Role NOT IN List
- **Method**: `GET`
- **Endpoint**: `/api/users/search/role/not-in?roles={roles}`
- **Auth Required**: Yes

---

## 3. Project Management APIs

### 3.1 Create Project
- **Method**: `POST`
- **Endpoint**: `/api/projects/create`
- **Description**: Creates a new project listing, sets `createdAt` timestamp, and automatically assigns `ownerId` as a `ProjectMember` with role `OWNER`.
- **Auth Required**: Yes

#### Request Body:
```json
{
  "ownerId": 1,
  "name": "ProjectMate Platform",
  "type": "Web Application",
  "description": "Collaborative project matching for developers.",
  "memberCount": 5,
  "visibility": "PUBLIC",
  "latestUpdate": "Backend initial release and setup complete.",
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
  "latestUpdate": "Backend initial release and setup complete.",
  "createdAt": "2026-09-26T11:00:00",
  "status": "OPEN"
}
```

---

### 3.2 Get Project By ID
- **Method**: `GET`
- **Endpoint**: `/api/projects/{id}`
- **Auth Required**: Yes

---

### 3.3 List Projects By Owner ID
- **Method**: `GET`
- **Endpoint**: `/api/projects?ownerId={ownerId}`
- **Auth Required**: Yes

---

### 3.4 Search Projects By Name Substring
- **Method**: `GET`
- **Endpoint**: `/api/projects/search?name={name}`
- **Auth Required**: Yes

---

### 3.5 Search Projects By Type
- **Method**: `GET`
- **Endpoint**: `/api/projects/search/type?type={type}`
- **Auth Required**: Yes

---

### 3.6 Search Projects By Visibility
- **Method**: `GET`
- **Endpoint**: `/api/projects/search/visibility?visibility={visibility}`
- **Example**: `/api/projects/search/visibility?visibility=PUBLIC`
- **Auth Required**: Yes

---

### 3.7 Search Projects By Status
- **Method**: `GET`
- **Endpoint**: `/api/projects/search/status?status={status}`
- **Auth Required**: Yes

---

### 3.8 Search Projects By Owner ID & Status
- **Method**: `GET`
- **Endpoint**: `/api/projects/search/owner-status?ownerId={ownerId}&status={status}`
- **Auth Required**: Yes

---

### 3.9 Search Project By Exact Name
- **Method**: `GET`
- **Endpoint**: `/api/projects/search/exact-name?name={name}`
- **Auth Required**: Yes

---

### 3.10 Update Project
- **Method**: `PUT`
- **Endpoint**: `/api/projects/update/{id}`
- **Description**: Updates project parameters (`name`, `type`, `description`, `visibility`, `latestUpdate`, `memberCount`, `status`).
- **Auth Required**: Yes

#### Request Body:
```json
{
  "name": "ProjectMate Platform V2",
  "visibility": "PUBLIC",
  "latestUpdate": "Integrated real-time matchmaking algorithms.",
  "status": "IN_PROGRESS"
}
```

#### Response (`200 OK`):
```json
{
  "id": 6,
  "name": "ProjectMate Platform V2",
  "visibility": "PUBLIC",
  "latestUpdate": "Integrated real-time matchmaking algorithms.",
  "status": "IN_PROGRESS"
}
```

---

### 3.11 Delete Project
- **Method**: `DELETE`
- **Endpoint**: `/api/projects/delete/{id}`
- **Auth Required**: Yes

#### Response (`204 No Content`): Empty response body.

---

## 4. Project Member Operations APIs

### 4.1 Add Member to Project
- **Method**: `POST`
- **Endpoint**: `/api/projects/{projectId}/members?userId={userId}&role={role}&roleInProject={roleInProject}`
- **Description**: Adds a user to project team membership.
- **Query Params**:
  - `userId` (Long, required)
  - `role` (String, optional, default: `"MEMBER"`) - Platform membership role (`OWNER`, `MEMBER`, `ADMIN`)
  - `roleInProject` (String, optional) - Project title (e.g. `"Frontend Engineer"`)
- **Auth Required**: Yes

#### Response (`200 OK`):
```json
{
  "id": 12,
  "projectId": 6,
  "user": {
    "id": 2,
    "name": "Alice Smith",
    "email": "alice@example.com"
  },
  "role": "MEMBER",
  "roleInProject": "Frontend Engineer",
  "assignedAt": "2026-09-26T11:15:00"
}
```

---

### 4.2 List All Project Members
- **Method**: `GET`
- **Endpoint**: `/api/projects/{projectId}/members`
- **Auth Required**: Yes

---

### 4.3 Check Membership Status
- **Method**: `GET`
- **Endpoint**: `/api/projects/{projectId}/members/check?userId={userId}`
- **Response (`200 OK`)**: `true` or `false`
- **Auth Required**: Yes

---

### 4.4 Check Owner Status
- **Method**: `GET`
- **Endpoint**: `/api/projects/{projectId}/owner/check?userId={userId}`
- **Response (`200 OK`)**: `true` or `false`
- **Auth Required**: Yes

---

### 4.5 Update Member Platform Role
- **Method**: `PUT`
- **Endpoint**: `/api/projects/{projectId}/members/{userId}/role?role={role}`
- **Auth Required**: Yes

---

### 4.6 Update Member Title in Project
- **Method**: `PUT`
- **Endpoint**: `/api/projects/{projectId}/members/{userId}/role-in-project?roleInProject={roleInProject}`
- **Auth Required**: Yes

---

### 4.7 Remove Member from Project
- **Method**: `DELETE`
- **Endpoint**: `/api/projects/{projectId}/members/{userId}`
- **Auth Required**: Yes

---

### 4.8 List All Projects for a User
- **Method**: `GET`
- **Endpoint**: `/api/projects/members/user/{userId}`
- **Auth Required**: Yes

---

## 5. Master Skill Catalog APIs

### 5.1 Create Skill in Master Catalog
- **Method**: `POST`
- **Endpoint**: `/api/skills`
- **Auth Required**: Yes

#### Request Body:
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
- **Auth Required**: Yes

---

### 5.3 Get Skill By ID
- **Method**: `GET`
- **Endpoint**: `/api/skills/{id}`
- **Auth Required**: Yes

---

### 5.4 Search Skill By Name
- **Method**: `GET`
- **Endpoint**: `/api/skills/search?name={name}`
- **Auth Required**: Yes

---

### 5.5 Update Skill Name
- **Method**: `PUT`
- **Endpoint**: `/api/skills/{id}`
- **Auth Required**: Yes

---

### 5.6 Delete Skill
- **Method**: `DELETE`
- **Endpoint**: `/api/skills/{id}`
- **Auth Required**: Yes

---

## 6. Project Tech Stack Requirements APIs

### 6.1 Add Required Skill to Project
- **Method**: `POST`
- **Endpoint**: `/api/project-skills`
- **Auth Required**: Yes

#### Request Body:
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

### 6.2 Get Requirement By ID
- **Method**: `GET`
- **Endpoint**: `/api/project-skills/{id}`
- **Auth Required**: Yes

---

### 6.3 List All Required Skills for a Project
- **Method**: `GET`
- **Endpoint**: `/api/project-skills/project/{projectId}`
- **Auth Required**: Yes

---

### 6.4 List All Projects Requiring a Skill
- **Method**: `GET`
- **Endpoint**: `/api/project-skills/skill/{skillId}`
- **Auth Required**: Yes

---

### 6.5 Filter Requirements By Skill Level
- **Method**: `GET`
- **Endpoint**: `/api/project-skills/project/{projectId}/level/{level}`
- **Auth Required**: Yes

---

### 6.6 Update Requirement Level
- **Method**: `PUT`
- **Endpoint**: `/api/project-skills/{id}?level={level}`
- **Auth Required**: Yes

---

### 6.7 Remove Skill Requirement By ID
- **Method**: `DELETE`
- **Endpoint**: `/api/project-skills/{id}`
- **Auth Required**: Yes

---

### 6.8 Remove Skill Requirement from Project
- **Method**: `DELETE`
- **Endpoint**: `/api/project-skills/project/{projectId}/skill/{skillId}`
- **Auth Required**: Yes

---

## 7. Project Roles Catalog APIs

### 7.1 Create Standard Role Title
- **Method**: `POST`
- **Endpoint**: `/api/project-roles`
- **Auth Required**: Yes

#### Request Body:
```json
{
  "name": "Frontend Engineer"
}
```

---

### 7.2 Get All Project Roles
- **Method**: `GET`
- **Endpoint**: `/api/project-roles`
- **Auth Required**: Yes

---

### 7.3 Get Project Role By ID
- **Method**: `GET`
- **Endpoint**: `/api/project-roles/{id}`
- **Auth Required**: Yes

---

### 7.4 Search Project Role By Name
- **Method**: `GET`
- **Endpoint**: `/api/project-roles/search?name={name}`
- **Auth Required**: Yes

---

### 7.5 Update Project Role
- **Method**: `PUT`
- **Endpoint**: `/api/project-roles/{id}`
- **Auth Required**: Yes

---

### 7.6 Delete Project Role
- **Method**: `DELETE`
- **Endpoint**: `/api/project-roles/{id}`
- **Auth Required**: Yes

---

## 8. AI Assistance & Project Generation APIs

### 8.1 Analyze Project Idea / Prompt
- **Method**: `POST`
- **Endpoint**: `/api/ai/analyze`
- **Description**: Uses Google AI Studio Gemini API (`gemini-2.5-flash`) to analyze a user prompt and extract structured project details, tech stack requirements, and 4 team member roles with required skill proficiencies (1-5).
- **Auth Required**: Yes

#### Request Body:
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
- **Description**: Analyzes the prompt and automatically persists the `Project`, required `Skills`, and 4 `Project Roles` in the database.
- **Auth Required**: Yes

---

### 8.3 Save AI Analysis as Project
- **Method**: `POST`
- **Endpoint**: `/api/ai/save-analysis?ownerId={ownerId}`
- **Description**: Persists an `AiProjectAnalysisResponse` object directly into the database as a new project.
- **Auth Required**: Yes

---

## 9. Notification Management APIs

### 9.1 Create Notification
- **Method**: `POST`
- **Endpoint**: `/api/notifications`
- **Auth Required**: Yes

#### Request Body:
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
- **Auth Required**: Yes

---

### 9.3 Get Unread Notifications for User
- **Method**: `GET`
- **Endpoint**: `/api/notifications/user/{userId}/unread`
- **Auth Required**: Yes

---

### 9.4 Get Unread Notification Count
- **Method**: `GET`
- **Endpoint**: `/api/notifications/user/{userId}/unread-count`
- **Response (`200 OK`)**: `3`
- **Auth Required**: Yes

---

### 9.5 Mark Notification as Read
- **Method**: `PUT`
- **Endpoint**: `/api/notifications/{id}/read?userId={userId}`
- **Auth Required**: Yes

---

### 9.6 Mark All Notifications as Read
- **Method**: `PUT`
- **Endpoint**: `/api/notifications/user/{userId}/read-all`
- **Auth Required**: Yes

---

### 9.7 Soft Delete Notification
- **Method**: `DELETE`
- **Endpoint**: `/api/notifications/{id}?userId={userId}`
- **Auth Required**: Yes

---

## 10. Invitation Management APIs

### 10.1 Send Project Invitation
- **Method**: `POST`
- **Endpoint**: `/api/invitations`
- **Description**: Sends a project invitation to a user and automatically dispatches an in-app notification to the receiver.
- **Auth Required**: Yes

#### Request Body:
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
  "expiresAt": "2026-10-15T00:00:00"
}
```

---

### 10.2 Get Received Invitations
- **Method**: `GET`
- **Endpoint**: `/api/invitations/receiver/{receiverId}`
- **Auth Required**: Yes

---

### 10.3 Get Pending Received Invitations
- **Method**: `GET`
- **Endpoint**: `/api/invitations/receiver/{receiverId}/pending`
- **Auth Required**: Yes

---

### 10.4 Get Sent Invitations
- **Method**: `GET`
- **Endpoint**: `/api/invitations/sender/{senderId}`
- **Auth Required**: Yes

---

### 10.5 Get Invitations for a Project
- **Method**: `GET`
- **Endpoint**: `/api/invitations/project/{projectId}`
- **Auth Required**: Yes

---

### 10.6 Accept Project Invitation
- **Method**: `PUT`
- **Endpoint**: `/api/invitations/{id}/accept?receiverId={receiverId}`
- **Description**: Accepts the invitation, auto-adds receiver as a `ProjectMember`, and notifies sender.
- **Auth Required**: Yes

---

### 10.7 Reject Project Invitation
- **Method**: `PUT`
- **Endpoint**: `/api/invitations/{id}/reject?receiverId={receiverId}`
- **Auth Required**: Yes

---

### 10.8 Cancel Project Invitation
- **Method**: `PUT`
- **Endpoint**: `/api/invitations/{id}/cancel?senderId={senderId}`
- **Auth Required**: Yes

---

## 11. User Skills Profile APIs

### 11.1 Add or Update User Skill
- **Method**: `POST`
- **Endpoint**: `/api/user-skills`
- **Description**: Maps a skill and proficiency (1-5) to a user profile. Accepts `skillId` or `skillName`.
- **Auth Required**: Yes

#### Request Body:
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

### 11.2 Get User Skill By ID
- **Method**: `GET`
- **Endpoint**: `/api/user-skills/{id}`
- **Auth Required**: Yes

---

### 11.3 List All Skills for a User
- **Method**: `GET`
- **Endpoint**: `/api/user-skills/user/{userId}`
- **Auth Required**: Yes

---

### 11.4 List All Users Having a Skill
- **Method**: `GET`
- **Endpoint**: `/api/user-skills/skill/{skillId}`
- **Auth Required**: Yes

---

### 11.5 Filter Users By Skill and Minimum Proficiency
- **Method**: `GET`
- **Endpoint**: `/api/user-skills/skill/{skillId}/min-proficiency/{minProficiency}`
- **Auth Required**: Yes

---

### 11.6 Update Skill Proficiency
- **Method**: `PUT`
- **Endpoint**: `/api/user-skills/{id}?proficiency={proficiency}`
- **Auth Required**: Yes

---

### 11.7 Delete User Skill By ID
- **Method**: `DELETE`
- **Endpoint**: `/api/user-skills/{id}`
- **Auth Required**: Yes

---

### 11.8 Remove Skill from User Profile
- **Method**: `DELETE`
- **Endpoint**: `/api/user-skills/user/{userId}/skill/{skillId}`
- **Auth Required**: Yes

---

## 12. Automated Team Referral & Matchmaking APIs

### 12.1 Suggest Team for Project
- **Method**: `GET`
- **Endpoint**: `/api/refer/suggest-team/{projectId}`
- **Description**: Ranks candidate users for each role required in a project based on `UserSkill` match scores.
- **Auth Required**: Yes

#### Response (`200 OK`):
```json
{
  "projectId": 6,
  "projectName": "ProjectMate Platform",
  "totalRolesRequired": 2,
  "roleRecommendations": [
    {
      "roleId": 1,
      "roleName": "Frontend Lead",
      "topCandidates": [
        {
          "candidate": {
            "id": 2,
            "name": "Alice Smith",
            "email": "alice@example.com",
            "about": "Senior React Developer",
            "profilePic": "https://example.com/alice.jpg"
          },
          "matchScore": 90.0,
          "totalRequiredSkills": 2,
          "matchedSkillsCount": 2,
          "matchedSkills": [
            {
              "skillName": "Next.js",
              "userProficiency": 5,
              "requiredProficiency": 5,
              "isMatched": true
            }
          ],
          "missingSkills": []
        }
      ]
    }
  ]
}
```

---

### 12.2 Suggest Team from AI Analysis / Custom Requirements
- **Method**: `POST`
- **Endpoint**: `/api/refer/suggest-team`
- **Description**: Evaluates platform users against custom or AI-generated role and skill requirements.
- **Auth Required**: Yes

#### Request Body:
```json
{
  "projectId": 6,
  "customRoles": [
    {
      "roleName": "Backend Engineer",
      "requiredSkills": [
        {
          "skillName": "Python",
          "proficiencyRequired": 5
        },
        {
          "skillName": "FastAPI",
          "proficiencyRequired": 4
        }
      ]
    }
  ]
}
```

---

### 12.3 Match Candidates for Specific Role
- **Method**: `GET`
- **Endpoint**: `/api/refer/match-users-for-role/{roleId}?projectId={projectId}`
- **Auth Required**: Yes

---

## 13. Health Check APIs

### 13.1 Health Status Check
- **Method**: `GET`
- **Endpoint**: `/api/health` 
- **Description**: Verifies operational health of the application.
- **Auth Required**: No

#### Response (`200 OK`):
```text
Server is up and running!
```
