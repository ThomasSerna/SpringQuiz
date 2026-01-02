# Spring Quiz App

A simple **quiz web application** built with **Spring Boot** and **Thymeleaf**.

It supports user registration and login, lets regular users take quizzes, and lets admins manage (create/edit/delete) quiz questions from the UI.

---

## Features

- **Authentication & roles**
  - Register and log in via web forms
  - Role-based views/permissions (e.g., `ROLE_ADMIN` vs regular users)

- **Quiz experience**
  - Users can view quizzes and submit answers
  - Feedback/validation handled by the backend

- **Admin panel**
  - Create, edit, and delete quiz questions

- **In-memory storage**
  - Quizzes are managed in-memory via a service layer (no database configured by default)

---

## Tech Stack

- Java + Spring Boot
- Spring MVC + Thymeleaf (server-side rendered views)
- Spring Security (login, authorization)
- Maven (build tool)

---

## Project Structure

Typical layout:

```
src/
 ├─ main/
 │   ├─ java/com/app/springquiz/
 │   │   ├─ config/          # security configuration
 │   │   ├─ controller/      # MVC controllers (routes)
 │   │   ├─ model/           # domain models (Quiz, User, etc.)
 │   │   └─ service/         # business logic (quiz & user services)
 │   └─ resources/
 │       ├─ templates/       # Thymeleaf pages
 │       └─ application.properties
 └─ test/
     └─ java/...             # tests
```

---


## Main Routes

> Exact routes can be checked in the controller(s). Common ones include:

- `/` or `/home` — home page (role-based view)
- `/login` — login page
- `/register` — registration page
- Admin quiz management routes (create/edit/delete)
