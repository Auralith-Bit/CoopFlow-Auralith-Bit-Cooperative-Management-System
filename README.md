# Cooperative Management System (CMS)

A web-based application for digitizing cooperative society operations — member management, savings, loans, accounting, and reporting.

## Tech Stack

| Layer    | Technology                         |
| -------- | ---------------------------------- |
| Frontend | Angular 17 (standalone components) |
| Backend  | Java 22, Spring Boot 3.2.5         |
| Database | PostgreSQL                         |
| Auth     | JWT (JJWT), Spring Security        |
| Build    | Maven, Angular CLI                 |

## Prerequisites

- **Java 22+** — verify: `java -version`
- **Node.js 20+** — verify: `node --version`
- **Maven 3.9+** — verify: `mvn --version`
- **PostgreSQL 16+** — running on `localhost:5432`
- **Angular CLI** — `npm install -g @angular/cli`

## Quick Start

### 1. Database

```bash
psql -U postgres -f database/init.sql
```

Creates the `cooperative_db` database, all tables, indexes, and seeds the default admin user.

Default admin: `admin` / `admin123`

### 2. Backend

```bash
cd backend
mvn spring-boot:run
```

Starts on `http://localhost:8080`. The `spring.jpa.hibernate.ddl-auto=update` setting will sync JPA entities with the database schema automatically.

### 3. Frontend

```bash
cd frontend
ng serve
```

Starts on `http://localhost:4200`. Proxies API calls to the backend.

## User Flow

### Registration
- **Admin** — pre-seeded in the database by `DataInitializer`
- **Manager, Accountant** — created by Admin via the **Users** management page
- **Member** — self-registers through the public registration form (always MEMBER role)

### Approval Flow
1. Member registers → account is **disabled**, membership status = **PENDING**
2. Member cannot login until approved
3. Admin or Manager visits **Member Approvals** page → Approves or Rejects
4. On approval → account is enabled, membership becomes ACTIVE → member can login

## Project Structure

```
co-operative project/
├── backend/
│   ├── pom.xml
│   └── src/main/java/com/cooperative/cms/
│       ├── config/          # SecurityConfig, CorsConfig, DataInitializer
│       ├── controller/      # Auth, Admin, Member, Savings, Loan, Transaction, Dashboard, MemberPortal
│       ├── dto/             # Request/Response DTOs
│       ├── entity/          # JPA entities & enums (User, Member, Loan, Savings, etc.)
│       ├── repository/      # Spring Data JPA repositories
│       ├── security/        # JWT provider, filter, UserDetailsService, SecurityUtil
│       └── service/         # Business logic layer
├── frontend/
│   └── src/app/
│       ├── components/
│       │   ├── admin/       # user management, member approval
│       │   ├── auth/        # login, register
│       │   ├── dashboard/   # analytics cards
│       │   ├── layout/      # sidebar, header
│       │   ├── loans/       # list, form, detail (admin)
│       │   ├── member/      # dashboard, passbook, loans, loan-apply, savings (self-service)
│       │   ├── members/     # list, form (admin)
│       │   ├── savings/     # list, form (admin)
│       │   └── transactions/# list, form
│       ├── guards/          # AuthGuard
│       ├── interceptors/    # AuthInterceptor (Bearer token)
│       ├── models/          # TypeScript interfaces
│       └── services/        # HTTP services for each domain
└── database/
    └── init.sql             # Full PostgreSQL schema + seed data
```

## API Endpoints

### Authentication
| Method | Endpoint             | Description      | Access        |
| ------ | -------------------- | ---------------- | ------------- |
| POST   | `/api/auth/login`    | Login            | Public        |
| POST   | `/api/auth/register` | Register as member | Public      |

### Admin — User Management
| Method | Endpoint                         | Description                 | Access  |
| ------ | -------------------------------- | --------------------------- | ------- |
| GET    | `/api/admin/users`               | List all users              | ADMIN   |
| POST   | `/api/admin/users`               | Create Accountant/Manager   | ADMIN   |
| DELETE | `/api/admin/users/{id}`          | Delete a user               | ADMIN   |

### Admin/Manager — Member Approval
| Method | Endpoint                         | Description                 | Access         |
| ------ | -------------------------------- | --------------------------- | -------------- |
| GET    | `/api/admin/members/pending`     | List pending registrations  | ADMIN, MANAGER |
| PUT    | `/api/admin/members/{id}/approve`| Approve member              | ADMIN, MANAGER |
| DELETE | `/api/admin/members/{id}/reject` | Reject member               | ADMIN, MANAGER |

### Members (Admin CRUD)
| Method | Endpoint                  | Description     | Access         |
| ------ | ------------------------- | --------------- | -------------- |
| GET    | `/api/members`            | List all        | ADMIN, MANAGER |
| GET    | `/api/members/{id}`       | Get by ID       | ADMIN, MANAGER |
| POST   | `/api/members`            | Create          | ADMIN, MANAGER |
| PUT    | `/api/members/{id}`       | Update          | ADMIN, MANAGER |
| DELETE | `/api/members/{id}`       | Delete          | ADMIN, MANAGER |

### Member Portal (Self-Service)
| Method | Endpoint                        | Description            | Access |
| ------ | ------------------------------- | ---------------------- | ------ |
| GET    | `/api/member/me`                | My profile             | MEMBER |
| GET    | `/api/member/dashboard`         | My dashboard stats     | MEMBER |
| GET    | `/api/member/passbook`          | My passbook + balance  | MEMBER |
| GET    | `/api/member/loans`             | My loans               | MEMBER |
| POST   | `/api/member/loans/apply`       | Apply for a loan       | MEMBER |
| POST   | `/api/member/savings/deposit`   | Make a deposit         | MEMBER |
| POST   | `/api/member/savings/withdraw`  | Make a withdrawal      | MEMBER |

### Savings
| Method | Endpoint                          | Description      | Access                     |
| ------ | --------------------------------- | ---------------- | -------------------------- |
| POST   | `/api/savings/deposit`            | Record deposit   | ADMIN, ACCOUNTANT, MANAGER |
| POST   | `/api/savings/withdraw`           | Record withdrawal| ADMIN, ACCOUNTANT, MANAGER |
| GET    | `/api/savings/member/{memberId}`  | Savings history  | All roles                  |
| GET    | `/api/savings/member/{memberId}/balance` | Balance    | All roles                  |

### Loans
| Method | Endpoint                   | Description      | Access              |
| ------ | -------------------------- | ---------------- | ------------------- |
| GET    | `/api/loans`               | List all loans   | ADMIN, MANAGER      |
| GET    | `/api/loans/{id}`          | Get by ID        | ADMIN, MANAGER      |
| POST   | `/api/loans`               | Apply for loan   | ADMIN, MANAGER      |
| PUT    | `/api/loans/{id}/approve`  | Approve loan     | ADMIN, MANAGER      |
| PUT    | `/api/loans/{id}/reject`   | Reject loan      | ADMIN, MANAGER      |
| POST   | `/api/loans/{id}/payment`  | Make payment     | ADMIN, MANAGER      |

### Transactions (Income/Expense)
| Method | Endpoint                     | Description            | Access                |
| ------ | ---------------------------- | ---------------------- | --------------------- |
| GET    | `/api/transactions`          | List all               | ADMIN, ACCOUNTANT     |
| GET    | `/api/transactions/range`    | Filter by date range   | ADMIN, ACCOUNTANT     |
| POST   | `/api/transactions`          | Create                 | ADMIN, ACCOUNTANT     |

### Dashboard
| Method | Endpoint            | Description               | Access              |
| ------ | ------------------- | ------------------------- | ------------------- |
| GET    | `/api/dashboard`    | Aggregated stats & counts | ADMIN, ACCOUNTANT, MANAGER |

## Role-Based Access

| Role       | Permissions                                                               |
| ---------- | ------------------------------------------------------------------------- |
| Admin      | Full system access; creates Manager & Accountant users                    |
| Accountant | Financial records, savings, transactions, reports                         |
| Manager    | Approve member registrations & loans, manage members, view reports        |
| Member     | Self-service: view own savings, passbook, apply for loans, view statements|

## Frontend Routes

| Path                   | Component            | Role                  |
| ---------------------- | -------------------- | --------------------- |
| `/login`               | Login                | Public                |
| `/register`            | Register             | Public                |
| `/dashboard`           | Admin Dashboard      | Non-Member            |
| `/members`             | Member List          | Admin, Manager        |
| `/members/new`         | Member Form          | Admin, Manager        |
| `/members/:id/edit`    | Member Form          | Admin, Manager        |
| `/savings`             | Savings List         | Admin, Accountant     |
| `/savings/new`         | Savings Form         | Admin, Accountant     |
| `/loans`               | Loan List            | Admin, Manager        |
| `/loans/new`           | Loan Form            | Admin, Manager        |
| `/loans/:id`           | Loan Detail          | Admin, Manager        |
| `/transactions`        | Transaction List     | Admin, Accountant     |
| `/transactions/new`    | Transaction Form     | Admin, Accountant     |
| `/admin/users`         | User Management      | Admin                 |
| `/admin/users/new`     | Create Staff User    | Admin                 |
| `/admin/member-approval` | Member Approvals   | Admin, Manager        |
| `/member/dashboard`    | Member Dashboard     | Member                |
| `/member/passbook`     | Passbook             | Member                |
| `/member/savings`      | Savings Self-Service | Member                |
| `/member/loans`        | My Loans             | Member                |
| `/member/loans/apply`  | Apply for Loan       | Member                |

## Features

- Dashboard with real-time analytics (member counts, loan stats, financial summaries)
- Self-service member portal (dashboard, passbook, savings, loans)
- Member registration with approval workflow (auto-generated member IDs)
- Admin user management (create Accountant & Manager accounts)
- Savings deposits & withdrawals with balance tracking
- Loan application, approval workflow, and EMI calculation
- Loan repayment tracking (principal/interest breakdown)
- Income/expense accounting
- JWT-based authentication with role-based authorization
- Lazy-loaded Angular routes for optimal performance

## Future Enhancements

- PDF report export
- Email/SMS notifications
- Online payment gateway integration
- Mobile app
- AI-based loan risk analysis
