# Jira Clone — Backend

REST API backend for the Jira Clone project management application, built with Java 21 and Spring Boot. Full multi-tenant workspace platform with real-time notifications, RBAC, and Stripe billing.

![Java](https://img.shields.io/badge/Java_21-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=flat&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat&logo=docker&logoColor=white)
![CI](https://img.shields.io/badge/CI-GitHub_Actions-2088FF?style=flat&logo=github-actions&logoColor=white)
![Stripe](https://img.shields.io/badge/Stripe-635BFF?style=flat&logo=stripe&logoColor=white)

🔗 **Live App:** [jiraclone-frontend.vercel.app](https://jiraclone-frontend.vercel.app)
🔗 **Frontend Repo:** [github.com/Karisha91/jiraclone-frontend](https://github.com/Karisha91/jiraclone-frontend)

**Demo Login:**
| Field | Value |
|-------|-------|
| Username | `admin123` |
| Password | `admin123` |

---

## Overview

A full-stack Jira-style project management tool supporting multiple workspaces, role-based permissions, Kanban boards with drag-and-drop, real-time notifications, and subscription billing via Stripe. Built to production-grade standards: authentication, authorization, rate limiting, audit logging, and a CI/CD pipeline with branch protection.

---

## Tech Stack

- Java 21
- Spring Boot 3.5
- Spring Security + JWT Authentication (role claims, `@PreAuthorize` RBAC)
- PostgreSQL / JPA / Hibernate
- WebSockets — STOMP / SockJS (real-time notifications)
- Stripe (Checkout Sessions, webhook signature verification)
- Cloudinary (avatar uploads)
- Bucket4j (rate limiting on auth endpoints)
- JavaMailSender / Mailtrap (transactional email — password reset, assignment notifications)
- Docker (multi-stage Dockerfile + docker-compose)
- Maven
- Railway (deployment)
- Neon PostgreSQL (production database)

---

## Architecture

```
Controller Layer   →  REST API endpoints
Service Layer      →  Business logic
Repository Layer   →  JPA / Hibernate (PostgreSQL)
Security Layer     →  JWT authentication + Spring Security + RBAC
Realtime Layer     →  WebSocket (STOMP/SockJS) notifications
```

---

## Key Features

- **Multi-tenant workspaces** — full CRUD, member invites/removal, workspace-scoped projects and dashboards
- **Kanban board** — drag-and-drop issue management with position-based ordering
- **Real-time notifications** — WebSocket push + persisted notification history
- **RBAC** — role-based access control enforced via `@PreAuthorize` and JWT role claims
- **Stripe billing** — FREE / PREMIUM / EXPIRED subscription tiers, Checkout Session flow, webhook verification
- **Audit logging** — tracked actions across issues, comments, and workspace changes
- **Rate limiting** — Bucket4j-backed protection on authentication endpoints
- **Email flows** — password reset (UUID token) and issue assignment notifications
- **Avatar uploads** — Cloudinary-backed profile photos
- **Paginated comments** — `Page<Comment>` backend with "load more" frontend pattern

---

## API Endpoints

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login and receive JWT token |
| POST | `/api/auth/forgot-password` | Request password reset email |
| POST | `/api/auth/reset-password` | Reset password via token |

### Workspaces
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/workspaces` | Get all workspaces for user |
| POST | `/api/workspaces` | Create new workspace |
| GET | `/api/workspaces/{id}` | Get workspace by ID |
| POST | `/api/workspaces/{id}/members` | Add member to workspace |
| DELETE | `/api/workspaces/{id}/members/{userId}` | Remove member from workspace |

### Projects
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/workspace/{workspaceId}/projects` | Get all projects in workspace |
| POST | `/api/projects` | Create new project |
| DELETE | `/api/projects/{id}` | Delete project |

### Issues
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/issues` | Get all issues |
| GET | `/api/issues/{id}` | Get issue by ID |
| GET | `/api/issues/project/{id}` | Get issues by project |
| GET | `/api/issues/board/{projectId}` | Get issues grouped for Kanban board |
| POST | `/api/issues` | Create new issue |
| PUT | `/api/issues/{id}` | Update issue |
| PATCH | `/api/issues/{id}/move` | Move issue (Kanban drag-and-drop) |
| DELETE | `/api/issues/{id}` | Delete issue |

### Comments
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/comments/{issueId}` | Get paginated comments by issue |
| POST | `/api/comments` | Add comment |
| DELETE | `/api/comments/{id}` | Delete comment (author-authorized) |

### Notifications
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/notifications` | Get notifications for user |
| WS | `/ws` (STOMP) | Real-time notification push |

### Billing (Stripe)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/billing/checkout` | Create Stripe Checkout session |
| POST | `/api/billing/webhook` | Stripe webhook (signature-verified) |

### Profile
| Method | Endpoint | Description |
|--------|----------|-------------|
| PUT | `/api/profile/username` | Change username (JWT refresh) |
| PUT | `/api/profile/password` | Change password |
| POST | `/api/profile/avatar` | Upload avatar (Cloudinary) |

---

## Testing & CI/CD

### Tests — 62 passing tests total

- **Backend:** 27 unit tests — JUnit 5 + Mockito (AAA pattern), full service-layer coverage with mocked repositories
- **Frontend:** 35 tests — Vitest, React Testing Library, MSW, covering Login, Register, Dashboard, Projects, and Issue pages

### CI/CD — GitHub Actions
- Automated test runs on every Pull Request with a PostgreSQL service container
- Branch protection on master — direct pushes blocked
- Tests must pass before merge is allowed
- Railway auto-deploys on merge

---

## Running Locally with Docker 🐳

The easiest way to run the backend locally. No need to install Java, Maven, or PostgreSQL.

**Prerequisites:** Docker Desktop installed and running.

```bash
git clone https://github.com/Karisha91/jiraclone
cd jiraclone
docker-compose up --build
```

Backend will be available at `http://localhost:8080`.

---

## Running Locally without Docker

**Prerequisites:** Java 21, Maven, PostgreSQL

```bash
git clone https://github.com/Karisha91/jiraclone
cd jiraclone
```

Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/jiraclone
spring.datasource.username=your_username
spring.datasource.password=your_password
```

```bash
./mvnw spring-boot:run
```

---

## Environment Variables

| Variable | Description |
|----------|-------------|
| `DATABASE_URL` | PostgreSQL connection URL |
| `DATABASE_USERNAME` | Database username |
| `DATABASE_PASSWORD` | Database password |
| `JWT_SECRET` | Secret key for JWT signing |
| `STRIPE_SECRET_KEY` | Stripe API secret key |
| `STRIPE_WEBHOOK_SECRET` | Stripe webhook signing secret |
| `CLOUDINARY_URL` | Cloudinary connection string |
| `MAIL_USERNAME` | SMTP username (Mailtrap) |
| `MAIL_PASSWORD` | SMTP password (Mailtrap) |

---

## Author

**Ivan Djurdjević** — Career changer from 14 years of retail management to full-stack development. Self-taught developer, building production-grade projects from the ground up.

[GitHub](https://github.com/Karisha91) · [LinkedIn](https://linkedin.com/in/ivandjurdjevic)