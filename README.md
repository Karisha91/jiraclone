# Jira Clone — Backend

REST API backend for the Jira Clone project management application, built with Java 21 and Spring Boot.

![Java](https://img.shields.io/badge/Java_21-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=flat&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat&logo=docker&logoColor=white)
![CI](https://img.shields.io/badge/CI-GitHub_Actions-2088FF?style=flat&logo=github-actions&logoColor=white)

🔗 **Frontend:** [jiraclone-frontend.vercel.app](https://jiraclone-frontend.vercel.app)  
🔗 **Frontend Repo:** [github.com/Karisha91/jiraclone-frontend](https://github.com/Karisha91/jiraclone-frontend)

---

## Tech Stack

- Java 21
- Spring Boot 3.5
- Spring Security + JWT Authentication
- PostgreSQL / JPA / Hibernate
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
Security Layer     →  JWT authentication + Spring Security
```

---

## API Endpoints

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login and receive JWT token |

### Projects
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/projects` | Get all projects |
| POST | `/api/projects` | Create new project |
| DELETE | `/api/projects/{id}` | Delete project |

### Issues
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/issues` | Get all issues |
| GET | `/api/issues/{id}` | Get issue by ID |
| GET | `/api/issues/project/{id}` | Get issues by project |
| POST | `/api/issues` | Create new issue |
| PUT | `/api/issues/{id}` | Update issue |
| DELETE | `/api/issues/{id}` | Delete issue |

### Comments
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/comments/{issueId}` | Get comments by issue |
| POST | `/api/comments` | Add comment |
| DELETE | `/api/comments/{id}` | Delete comment |

---

## Testing & CI/CD

### Tests — 27 passing unit tests

- JUnit 5 + Mockito (AAA pattern)
- Service layer unit tests with mocked repositories
- Full coverage of business logic across all services

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

---

## Author

**Ivan Djurdjevic** — Career changer from 14 years of retail management to full-stack development. Self-taught, 9 months of coding experience.

[GitHub](https://github.com/Karisha91) · [LinkedIn](https://linkedin.com/in/your-profile)