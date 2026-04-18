# 🏢 Employee Management System

A secure, production-ready RESTful API for managing company employees, departments, positions, and users — 
built with **Java Spring Boot**, secured with **Keycloak (OAuth2 + JWT)**, and containerized with **Docker**.

---

## 🚀 Tech Stack

| Technology | Purpose |
|---|---|
| Java | Core programming language |
| Spring Boot | Backend framework |
| Spring Security + OAuth2 | Security layer |
| Keycloak Admin Client | User registration, password reset, email verification |
| PostgreSQL 16 | Relational database |
| Docker & Docker Compose | Containerization |
| Swagger UI | Interactive API documentation |
| Gradle | Build tool |
| Lombok | Boilerplate reduction |
| MapStruct | Entity-DTO mapping |

---

## 📌 Features

- ✅ **Employee Management** — Create, update, soft-delete, search, filter by status/department, paginate
- ✅ **Department Management** — Create, update, soft-delete, paginate
- ✅ **Position Management** — Create, update, soft-delete, paginate
- ✅ **User Management** — Create, update, soft-delete, search by keyword, lookup by Keycloak ID
- ✅ **Auth Service** — Register user in Keycloak + local DB, forgot password, reset password via Keycloak email actions
- ✅ **Email Verification** — Sends `VERIFY_EMAIL` action email on registration; `UPDATE_PASSWORD` on forgot password
- ✅ **Role-Based Access Control (RBAC)** — `ADMIN`, `HR`, `USER` with method-level `@PreAuthorize`
- ✅ **Keycloak Integration** — OAuth2 resource server + Keycloak Admin Client (`CLIENT_CREDENTIALS` grant)
- ✅ **Custom JWT Converter** — Extracts roles from `resource_access.spring-boot.roles` with `ROLE_` prefix
- ✅ **Audit Logging** — Tracks CREATE, UPDATE, DELETE with performer, entity, and timestamp
- ✅ **JPA Auditing** — Auto-captures `createdAt`, `updatedAt`, `createdBy`, `updatedBy`
- ✅ **Soft Delete** — Records flagged as deleted, never permanently removed
- ✅ **Report & Export** — Export employee data as **CSV** or **Excel (.xlsx)** filtered by department, position, status
- ✅ **Global Exception Handling** — Consistent structured error responses
- ✅ **Pagination & Search** — All list endpoints support pageable queries
- ✅ **Swagger UI** — Publicly accessible API documentation

---

## 👥 User Roles & Permissions

| Role | Permissions |
|---|---|
| `ADMIN` | Full access — all endpoints including users, audit logs, reports, exports |
| `HR` | Manage employees, departments, positions, view users |
| `USER` | Create, view, update, delete employees; view departments and positions |

---

## 📡 API Endpoints

### 🔐 Auth — `/api/v1/auth`
| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/register` | Register new user in Keycloak + local DB | Public |
| POST | `/forgot-password?email=` | Send UPDATE_PASSWORD email via Keycloak | Public |
| PUT | `/reset-password` | Reset user password in Keycloak | Public |

### 👤 Employees — `/api/v1/employees`
| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/` | Create employee | ADMIN, HR, USER |
| GET | `/` | Get all employees (paginated) | ADMIN, HR, USER |
| GET | `/{uuid}` | Get employee by UUID | ADMIN, HR, USER |
| PATCH | `/{uuid}` | Update employee | ADMIN, HR, USER |
| DELETE | `/{uuid}` | Soft delete employee | ADMIN, HR, USER |
| GET | `/status?status=` | Filter by status | ADMIN, HR |
| GET | `/search?keyword=` | Search by name or email | ADMIN, HR |
| GET | `/department/{departmentUuid}` | Filter by department | ADMIN, HR |

### 🏢 Departments — `/api/v1/departments`
| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/` | Create department | ADMIN, HR |
| GET | `/` | Get all departments (paginated) | ADMIN, HR, USER |
| GET | `/{uuid}` | Get department by UUID | ADMIN, HR, USER |
| PATCH | `/{uuid}` | Update department | ADMIN, HR |
| DELETE | `/{uuid}` | Soft delete department | ADMIN, HR |

### 💼 Positions — `/api/v1/positions`
| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/` | Create position | ADMIN, HR |
| GET | `/` | Get all positions (paginated) | ADMIN, HR, USER |
| GET | `/{uuid}` | Get position by UUID | ADMIN, HR, USER |
| PATCH | `/{uuid}` | Update position | ADMIN, HR |
| DELETE | `/{uuid}` | Soft delete position | ADMIN, HR |

### 🙍 Users — `/api/v1/users`
| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/` | Create user | ADMIN |
| GET | `/` | Get all users (paginated) | ADMIN, HR |
| GET | `/{uuid}` | Get user by UUID | ADMIN, HR |
| GET | `/keycloak/{keycloakId}` | Get user by Keycloak ID | ADMIN, HR |
| GET | `/search?keyword=` | Search users by keyword | ADMIN, HR |
| PATCH | `/{uuid}` | Update user | ADMIN |
| DELETE | `/{uuid}` | Soft delete user | ADMIN |

### 📊 Reports & Export — `/api/v1/reports/employees`
| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/csv` | Export employees as CSV | ADMIN |
| GET | `/excel` | Export employees as Excel (.xlsx) | ADMIN |

> Supports optional filters: `?departmentUuid=`, `?positionUuid=`, `?status=`

### 📋 Audit Logs — `/api/v1/audit-logs`
| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/` | Get audit logs (filterable, paginated) | ADMIN |

> Supports filters: `entityName`, `entityUuid`, `performedBy`, `fromDate`, `toDate`

---

## 🗄️ Data Model Overview

| Entity | Key Fields |
|---|---|
| `Employee` | firstName, lastName, email, phone, status, department, position |
| `Department` | name, description |
| `Position` | name, level |
| `User` | username, email, role (ADMIN/HR/USER), keycloakId, linked to Employee |
| `AuditLogging` | entityName, entityUuid, action (CREATE/UPDATE/DELETE), performedBy, performedAt |
| `Audit` (base) | createdAt, updatedAt, createdBy, updatedBy (inherited by all entities) |

> All entities use **UUID** as business identifier and support **soft delete** (`isDeleted` flag).

---

## 🔐 Security

- OAuth2 Resource Server with **Keycloak JWT**
- **Keycloak Admin Client** configured with `CLIENT_CREDENTIALS` grant type for admin operations
- Custom **JWT Converter** (`JwtConverterConfigure`) extracts roles from `resource_access.spring-boot.roles` and maps them with `ROLE_` prefix for `@PreAuthorize`
- Username resolved from `preferred_username` claim (falls back to `subject`)
- Keycloak Realm: `springboot` | Client ID: `spring-boot`
- Method-level security via `@PreAuthorize`
- Swagger UI publicly accessible
- All `/api/v1/**` endpoints require valid JWT token

---

## ⚙️ Configuration

```yaml
server:
  port: 9090

spring:
  application:
    name: spring-boot-restful-api
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8080/realms/springboot
  datasource:
    url: jdbc:postgresql://localhost:5432/employee_db001
    username: postgres
    password: 123
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: update

keycloak:
  server-url: http://localhost:8080
  main-realm: springboot
  client-id: spring-boot
  client-secret: <your-client-secret>

springdoc:
  swagger-ui:
    path: /api-docs
    enabled: true
  api-docs:
    path: /api-docs/json
```

---

## 🛠️ Getting Started

### Prerequisites
- Java 17+
- Docker & Docker Compose
- Keycloak server (realm: `springboot`, client: `spring-boot`)

### Run with Docker

```bash
# Clone the repository
git clone https://github.com/ekvitou/ekvitou.k-employee-management.git

# Navigate to project
cd ekvitou.k-employee-management

# Start all services (App + PostgreSQL)
docker-compose up --build
```

### Access
- **API Base URL:** `http://localhost:2222`
- **Swagger UI:** `http://localhost:2222/swagger-ui/index.html`
- **API Docs:** `http://localhost:2222/v3/api-docs`
- **PostgreSQL:** `localhost:5432` → DB: `employee_db001`

> Note: App runs internally on port `9090`, exposed as `2222` via Docker.

### Authentication
1. Login via Keycloak realm `springboot` to obtain access token
2. Pass token in every request header:
```
Authorization: Bearer <your_token>
```

---

## 📁 Project Structure

```
src/
├── authService/        # Auth controller & service (register, forgot/reset password via Keycloak)
├── controller/         # REST controllers (Employee, Department, Position, User, Report, AuditLog)
├── keycloak/           # KeycloakConfigure (Admin Client bean setup)
├── model/
│   ├── dto/            # Request & response DTOs
│   ├── entity/         # JPA entities + Audit base class
│   ├── repository/     # Spring Data JPA repositories
│   └── service/        # Service interfaces & implementations
├── mapper/             # MapStruct mappers (entity ↔ DTO)
├── security/           # SecurityConfig & JwtConverterConfigure
├── exception/          # Custom exceptions (NotFound, Duplicate, AlreadyDeleted, etc.)
└── utils/              # GlobalExceptionHandler & ResponseTemplate
```

---

## ⚠️ Exception Handling

Global exception handler covers:
- `NotFoundException` — Employee, User, Department, Position not found (404)
- `AlreadyDeletedException` — Resource already soft-deleted (404)
- `DuplicateException` — Duplicate email/name/username conflict (409)
- `InvalidRequestException` — Bad request or invalid status (400)
- `KeycloakOperationException` — Keycloak service error (503)
- `DataIntegrityViolationException` — Database constraint violation (400)
- `MethodArgumentNotValidException` — Validation errors (400)
- `UnhandledException` — Unexpected server error (500)

---

## 👨‍💻 Author

**Ekvitou Kong**
- GitHub: [@ekvitou](https://github.com/ekvitou)
- Role: Java Spring Boot Backend Developer

---

## 📜 License

This project is built for portfolio and learning purposes.
