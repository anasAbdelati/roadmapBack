# Roadmap Generator

A full-stack web application that generates personalized visual learning roadmaps powered by AI.

## What it does

Users describe a skill or topic they want to learn. The application sends that input to a Gemini LLM via Spring AI, which returns a structured roadmap. The roadmap is rendered as an interactive graph using Cytoscape.js, showing nodes ordered from foundational concepts to advanced topics with directional links between steps.

## Features

- User registration and authentication with JWT access and refresh token flow
- Secure password hashing with BCrypt
- AI-powered roadmap generation based on free-text user input
- Visual graph rendering of the generated roadmap
- Roadmap persistence linked to authenticated users
- Protected endpoints requiring valid JWT

## Tech Stack

**Backend**
- Java 21, Spring Boot 3
- Spring Security with stateless JWT authentication
- Spring AI with Google Gemini
- MongoDB for users and roadmaps
- JUnit 5, Mockito for unit testing
- Cucumber for acceptance testing

**Frontend**
- Angular, TypeScript
- Cytoscape.js for graph rendering

**DevOps**
- Docker
- GitHub Actions CI/CD
- Kubernetes via Helm
- HashiCorp Vault for secrets management

## API Endpoints

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | /user/register | Public | Create a new account |
| POST | /auth/login | Public | Authenticate and receive tokens |
| POST | /auth/refresh | Public | Refresh access token |
| POST | /api/v1/roadmap/generate | Required | Generate a roadmap |

## Architecture

The backend follows a layered architecture with clear separation between controllers, services, and repositories. Authentication concerns are isolated in AuthService while user management is handled by UserService. JWT utilities are encapsulated in a dedicated component used by the security filter chain.

## Running locally

Requirements: Java 21, MongoDB, a Google Gemini API key

Set the following environment variables or provide them in application-local.properties:

```
spring.data.mongodb.uri=
spring.ai.google.genai.api-key=
jwt.secret=
jwt.accessExpiration=
jwt.refreshExpiration=
```

Then run:

```
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```