# Proposito365 Backend

**This app and documentation is still in development**

Proposito365 is a web application that allows users to create and share New Year’s resolutions within groups, revealing them the following year. This repository contains the **backend and infrastructure** components, including the Spring Boot API, authentication, database, and Dockerized microservices.

The frontend is maintained in a separate repository and communicates with this backend via REST APIs.

---

## Table of Contents
1. [Project Overview](#project-overview)
2. [Tech Stack](#tech-stack)
3. [System Architecture](#system-architecture)
4. [Backend Implementation](#backend-implementation)
5. [Docker & Infrastructure](#docker--infrastructure)

---

## Project Overview

**Proposito365** allows users to:

- Register and manage their accounts  
- Create and join groups  
- Store personal New Year’s resolutions  
- Share resolutions with group members  
- Reveal resolutions from previous years

This repository focuses on **backend logic, authentication, and infrastructure management**.

---

## Tech Stack

| Component        | Technology / Tool                       |
|-----------------|----------------------------------------|
| Backend          | Java, Spring Boot                       |
| Authentication  | Spring Security, JWT, HTTP-only Cookies, Refresh Tokens |
| Database         | MySQL                                   |
| Infrastructure   | Docker (3 microservices: frontend, backend, db) |
| Deployment       | AWS and DigitalOcean    |
| Web Server       | Nginx (frontend container)              |
| Communication    | REST API                                |

---

## System Architecture

### Microservices Architecture

1. **Frontend Container**  
   - Nginx server serving static frontend content  
   - Handles requests and communicates with backend API  

2. **Backend Container**  
   - Spring Boot application serving REST APIs  
   - Handles user management, groups, resolutions, and authentication  

3. **Database Container**  
   - MySQL instance  
   - Stores users, groups, resolutions, JWT refresh tokens, and other persistent data  

### Authentication Flow

- Users authenticate via Spring Security  
- JWT is stored in a secure HTTP-only cookie  
- Refresh cookies are used to maintain session security and extend token validity  

### Deployment Flow

- Docker Compose manages the three services  
- Initial production deployment on AWS leveraging the free tier and plans to migrate to DigitalOcean for a cost optimized option

---

## Backend Implementation

### Key Modules

1. **User Module**  
   - Registration, login, profile management  

2. **Group Module**  
   - Create and manage groups  
   - Add users to groups  

3. **Resolution Module**  
   - Create resolutions linked to users  
   - Share resolutions within groups  
   - Logic to reveal resolutions in the following year  

4. **Security Module**  
   - JWT generation and verification  
   - Refresh token handling  
   - Cookie management  

### API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | /auth/register | Register a new user |
| POST   | /auth/login | Authenticate user and create a cookie with a JWT within it |
| POST   | /auth/logout | Delete the cookies which authenticates the user |
| POST   | /refresh | Create a new access cookie using the refresh cookie |

**In development...**

---

## Docker & Infrastructure

### Docker Setup

- Three containers managed via **Docker Compose**:  
  1. **frontend** → Nginx serving the frontend  
  2. **backend** → Spring Boot API  
  3. **db** → MySQL  

- **Networking:** Containers communicate through Docker’s internal network  
- **Persistence:** MySQL volume for data storage  

### Dockerfile & Compose Highlights

- Backend container builds a Spring Boot JAR and runs it  
- MySQL container uses volume mapping for data persistence  
- Frontend container serves static files with Nginx

---
