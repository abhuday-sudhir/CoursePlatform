# Course Platform

A comprehensive RESTful API platform for managing online courses, student enrollments, and progress tracking. Built with Spring Boot, this application provides secure authentication, advanced search capabilities, and detailed progress monitoring for educational content.

## 📋 Table of Contents

- [Overview](#overview)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Features](#features)
- [Data Model](#data-model)
- [API Endpoints](#api-endpoints)
- [Security Implementation](#security-implementation)
- [Search Functionality](#search-functionality)
- [Setup Instructions](#setup-instructions)
- [Configuration](#configuration)
- [Docker Support](#docker-support)

## 🎯 Overview

The Course Platform is a backend service that enables users to:
- Browse and search through course catalogs
- Enroll in courses
- Track learning progress at the subtopic level
- View detailed progress reports

The platform uses a hierarchical course structure (Course → Topic → Subtopic) and implements JWT-based authentication for secure access to protected endpoints.

## 🛠 Technology Stack

- **Framework**: Spring Boot 3.2.5
- **Language**: Java 17
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA / Hibernate
- **Security**: Spring Security with JWT (JSON Web Tokens)
- **API Documentation**: Swagger/OpenAPI 3 (SpringDoc)
- **Build Tool**: Maven
- **Libraries**:
  - Lombok (for reducing boilerplate code)
  - Jackson (for JSON processing)
  - JJWT (for JWT token generation and validation)
  - BCrypt (for password hashing)

## 🏗 Architecture

The application follows a **layered architecture** pattern:

```
┌─────────────────────────────────────┐
│         Controllers Layer            │  ← REST API endpoints
├─────────────────────────────────────┤
│         Services Layer               │  ← Business logic
├─────────────────────────────────────┤
│         Repository Layer             │  ← Data access
├─────────────────────────────────────┤
│         Entity Layer                 │  ← Domain models
└─────────────────────────────────────┘
```

### Key Components

1. **Controllers**: Handle HTTP requests and responses
   - `AuthController`: User registration and authentication
   - `CourseController`: Course browsing and search
   - `EnrollmentController`: Course enrollment management
   - `ProgressController`: Mark subtopics as complete
   - `ViewProgressController`: View learning progress

2. **Services**: Implement business logic
   - `AuthService`: User registration, login, JWT token generation
   - `CourseService`: Course retrieval and search functionality
   - `EnrollmentService`: Enrollment validation and creation
   - `ProgressService`: Progress tracking and validation
   - `ViewProgressService`: Progress calculation and reporting
   - `CurrentUserService`: Extract current authenticated user

3. **Repositories**: Data access layer using Spring Data JPA
   - Standard CRUD operations
   - Custom queries (especially for search functionality)

4. **Security Layer**:
   - `JwtFilter`: Intercepts requests to validate JWT tokens
   - `SpringSecurity`: Configures security rules and filters
   - `JwtUtil`: Utility for token generation and validation

5. **Exception Handling**:
   - `GlobalExceptionHandler`: Centralized exception handling
   - Custom exceptions: `ResourceNotFoundException`, `ConflictException`, `ForbiddenException`

## ✨ Features

### 1. User Authentication & Authorization
- **Registration**: Users can create accounts with email and password
- **Login**: Secure authentication using JWT tokens
- **Password Security**: Passwords are hashed using BCrypt
- **Token-based Access**: Protected endpoints require valid JWT tokens

### 2. Course Management
- **Browse Courses**: List all available courses with summary information
- **Course Details**: Retrieve detailed course information including topics and subtopics
- **Advanced Search**: Full-text search across course content with relevance ranking

### 3. Enrollment System
- **Course Enrollment**: Users can enroll in courses
- **Duplicate Prevention**: System prevents duplicate enrollments
- **Enrollment Tracking**: Tracks enrollment timestamps

### 4. Progress Tracking
- **Subtopic Completion**: Mark individual subtopics as completed
- **Progress Calculation**: Automatic calculation of completion percentage
- **Progress Reports**: View detailed progress for enrolled courses
- **Enrollment Validation**: Users can only track progress for enrolled courses

### 5. Search Capabilities
- **Full-Text Search**: PostgreSQL full-text search with ranking
- **Multiple Match Types**: 
  - Prefix matching (using `to_tsquery`)
  - Typo tolerance (using `similarity` function)
  - Partial matching (LIKE queries)
- **Relevance Ranking**: Results sorted by relevance score
- **Content Snippets**: Highlights matching content in search results

## 📊 Data Model

The application uses a hierarchical course structure:

```
Course (id, title, description)
  └── Topic (id, title)
      └── Subtopic (id, title, content)
```

### Entity Relationships

1. **User**
   - `id` (Primary Key)
   - `email` (Unique)
   - `password` (Hashed)

2. **Course**
   - `id` (Primary Key, String)
   - `title`
   - `description`
   - One-to-Many relationship with `Topic`

3. **Topic**
   - `id` (Primary Key, String)
   - `title`
   - Many-to-One relationship with `Course`
   - One-to-Many relationship with `Subtopic`

4. **Subtopic**
   - `id` (Primary Key, String)
   - `title`
   - `content` (TEXT field for rich content)
   - Many-to-One relationship with `Topic`
   - Has a `search_vector` column for full-text search (PostgreSQL tsvector)

5. **Enrollment**
   - `id` (Primary Key)
   - `user` (Many-to-One)
   - `course` (Many-to-One)
   - `enrolledAt` (Timestamp)
   - Unique constraint on `(user_id, course_id)`

6. **SubtopicProgress**
   - `id` (Primary Key)
   - `user` (Many-to-One)
   - `subtopic` (Many-to-One)
   - `completed` (Boolean)
   - `completedAt` (Timestamp)
   - Unique constraint on `(user_id, subtopic_id)`

## 🔌 API Endpoints

### Public Endpoints (No Authentication Required)

#### Authentication
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and receive JWT token

#### Course Browsing
- `GET /api/courses` - Get all courses (summary)
- `GET /api/courses/{id}` - Get course details by ID
- `GET /api/search?q={query}` - Search courses by query

### Protected Endpoints (JWT Token Required)

#### Enrollment
- `POST /api/courses/{courseId}/enroll` - Enroll in a course

#### Progress Tracking
- `POST /api/subtopics/{subtopicId}/complete` - Mark subtopic as complete
- `POST /api/enrollments/{enrollmentId}/progress` - View progress for an enrollment

### API Documentation

Swagger UI is available at: `https://courseplatform-vema.onrender.com/swagger-ui/index.html`

The API documentation is organized into 5 tag groups:
1. Course APIs
2. Sign up and Login APIs
3. Enrollment APIs
4. Progress Tracking APIs
5. View Progress APIs

## 🔒 Security Implementation

### Authentication Flow

1. **Registration**:
   - User provides email and password
   - Email uniqueness is validated
   - Password is hashed using BCrypt
   - User is saved to database

2. **Login**:
   - User provides email and password
   - Spring Security's `AuthenticationManager` validates credentials
   - Upon successful authentication, a JWT token is generated
   - Token includes user email as subject
   - Token expiration is configurable (default: 2 hours)

3. **Token Validation**:
   - `JwtFilter` intercepts all requests
   - Extracts token from `Authorization: Bearer <token>` header
   - Validates token signature and expiration
   - Sets authentication context for authorized requests

### Security Configuration

- **Public Endpoints**: Authentication and course browsing endpoints are publicly accessible
- **Protected Endpoints**: Enrollment and progress tracking require valid JWT tokens
- **CSRF Protection**: Disabled (stateless API)
- **Password Encoding**: BCrypt with default strength (10 rounds)

### JWT Token Structure

- **Algorithm**: HS256 (HMAC SHA-256)
- **Claims**: 
  - `sub`: User email
  - `iat`: Issued at timestamp
  - `exp`: Expiration timestamp
- **Secret Key**: Configurable via `jwt.secret` property

## 🔍 Search Functionality

The search feature uses PostgreSQL's advanced text search capabilities:

### Search Strategy

1. **Full-Text Search**: Uses PostgreSQL's `tsvector` and `to_tsquery` for prefix matching
2. **Fuzzy Matching**: Uses `similarity()` function for typo tolerance (threshold: 0.4)
3. **Partial Matching**: Uses `LIKE` queries for substring matching

### Search Query

The search query combines multiple matching strategies:

```sql
WHERE
  s.search_vector @@ to_tsquery('english', :query || ':*')  -- Prefix match
  OR similarity(s.title, :query) > 0.4                        -- Typo match
  OR LOWER(s.title) LIKE LOWER(CONCAT('%', :query, '%'))     -- Partial match
```

### Ranking Algorithm

Results are ranked using:
- `ts_rank_cd()`: PostgreSQL's text search ranking
- `similarity()`: Fuzzy match score
- Combined score determines relevance

### Result Format

Search results include:
- Course information
- Match type (subtopic title match vs content match)
- Relevance snippet (highlighted matching text)
- Sorted by relevance score

## 🚀 Setup Instructions

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+ (with `pg_trgm` extension for similarity search)
- Docker (optional, for containerized deployment)

### Step 1: Database Setup

1. Create a PostgreSQL database:
```sql
CREATE DATABASE course;
```

2. Enable required extensions:
```sql
\c course
CREATE EXTENSION IF NOT EXISTS pg_trgm;
```

### Step 2: Configuration

1. Update `src/main/resources/application-dev.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/course
spring.datasource.username=your_username
spring.datasource.password=your_password
```

2. Configure JWT settings:
```properties
jwt.secret=your-secret-key-here-minimum-32-characters
jwt.expiration=7200000  # 2 hours in milliseconds
```

### Step 3: Build and Run

1. Build the project:
```bash
mvn clean install
```

2. Run the application:
```bash
mvn spring-boot:run
```

Or use the Maven wrapper:
```bash
./mvnw spring-boot:run
```

### Step 4: Verify

1. The application starts on `http://localhost:8080`
2. Access Swagger UI: `http://localhost:8080/swagger-ui.html`
3. Check health endpoint: `http://localhost:8080/api/health-check`

### Seed Data

The application automatically loads seed data from `src/main/resources/seed-data.json` on first startup. The `DataLoader` component checks if courses exist before loading, preventing duplicate data.

## ⚙️ Configuration

### Application Properties

#### Development Profile (`application-dev.properties`)
- Database connection settings
- JWT configuration
- Hibernate settings (DDL auto-update, SQL logging)

#### Production Profile (`application-prod.properties`)
- Production database settings
- Security configurations
- Performance optimizations

### Environment Variables

The application supports environment variable overrides:
- `PORT`: Server port (default: 8080)
- Database credentials can be overridden via environment variables

### JWT Configuration

- `jwt.secret`: Secret key for signing tokens (minimum 32 characters recommended)
- `jwt.expiration`: Token expiration time in milliseconds


## 📝 API Usage Examples

### 1. Register a User

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123"
  }'
```

### 2. Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123"
  }'
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "user@example.com",
  "expiresIn": 86400
}
```

### 3. Search Courses

```bash
curl http://localhost:8080/api/search?q=physics
```

### 4. Enroll in Course

```bash
curl -X POST http://localhost:8080/api/courses/physics-101/enroll \
  -H "Authorization: Bearer <your-token>"
```

### 5. Mark Subtopic Complete

```bash
curl -X POST http://localhost:8080/api/subtopics/speed/complete \
  -H "Authorization: Bearer <your-token>"
```

### 6. View Progress

```bash
curl -X POST http://localhost:8080/api/enrollments/1/progress \
  -H "Authorization: Bearer <your-token>"
```

## 🧪 Testing

Run tests using Maven:

```bash
mvn test
```

## 📦 Project Structure

```
CoursePlatform/
├── src/
│   ├── main/
│   │   ├── java/com/project/CoursePlatform/
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── Dto/             # Data Transfer Objects
│   │   │   ├── entity/          # JPA entities
│   │   │   ├── exception/       # Custom exceptions
│   │   │   ├── filter/          # Security filters
│   │   │   ├── repository/      # Data repositories
│   │   │   ├── Security/        # Security components
│   │   │   ├── seed/            # Data seeding
│   │   │   ├── service/         # Business logic
│   │   │   └── utils/           # Utility classes
│   │   └── resources/
│   │       ├── application*.properties
│   │       └── seed-data.json
│   └── test/                     # Test files
├── Dockerfile
├── pom.xml
└── README.md
```

## 🔧 Key Design Decisions

1. **JWT Authentication**: Stateless authentication for scalability
2. **Hierarchical Course Structure**: Flexible content organization
3. **PostgreSQL Full-Text Search**: Native database search for performance
4. **Progress Tracking at Subtopic Level**: Granular progress monitoring
5. **DTO Pattern**: Separation of API contracts from entities
6. **Global Exception Handling**: Consistent error responses
7. **CommandLineRunner for Seeding**: Automatic data initialization

