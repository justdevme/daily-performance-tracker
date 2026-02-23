Dưới đây là **README chuẩn portfolio-level** dành riêng cho project của bạn (Daily Performance Tracker + Focus Session).
Bạn có thể **copy dán trực tiếp** vào `README.md` trên GitHub — mình viết theo format mà recruiter backend rất thích (rõ kiến trúc + feature + tech stack).

---

# 📊 Daily Performance Tracker (DPT)

> A productivity and focus management system that helps users plan weekly tasks, track focused work sessions (Pomodoro-style), and analyze personal performance over time.

---

## 🚀 Overview

**Daily Performance Tracker (DPT)** is a full-stack productivity application designed to help users:

* Plan weekly goals and tasks
* Run structured **Focus Sessions** (Pomodoro technique)
* Track focused working time automatically
* Monitor productivity statistics and progress

The system integrates a **React frontend** with a **Spring Boot backend**, following clean architecture principles and RESTful API design.

---

## ✨ Key Features

### ✅ Task Management

* Create, update, delete weekly tasks
* Assign:

  * Priority (LOW / MEDIUM / HIGH)
  * Status (PENDING / IN_PROGRESS / COMPLETED)
  * Day of week & due date
* Organized by weekly planning workflow

---

### ⏱ Focus Session (Core Feature)

Pomodoro-style focus tracking integrated with backend persistence.

* Start focus session linked to a task
* Pause / Reset timer locally
* Stop session → automatically saved to database
* Auto duration calculation on backend
* Support:

  * Work session
  * Short break
  * Long break

Backend ensures accurate tracking even if frontend refreshes.

---

### 📈 Productivity Analytics

* Sessions completed today
* Total focused hours
* Completed focus sessions per task
* Historical focus data stored server-side

---

### 🔐 Backend Logic Highlights

* Session lifecycle management:

  * RUNNING
  * COMPLETED
  * CANCELED
* Server-side duration computation
* RESTful API design
* DTO mapping layer
* Transactional service handling

---

## 🏗 System Architecture

```
Frontend (React)
        │
        │ REST API (JSON)
        ▼
Spring Boot Backend
 ├── Controller Layer
 ├── Service Layer
 ├── Repository Layer (JPA)
 └── PostgreSQL Database
```

---

## 🧱 Tech Stack

### Frontend

* React.js
* React Router
* CSS (Glassmorphism UI)
* Fetch API wrapper

### Backend

* Java 21
* Spring Boot
* Spring Data JPA
* Hibernate
* RESTful APIs
* Lombok

### Database

* PostgreSQL

### Dev Tools

* Git & GitHub
* Postman
* IntelliJ IDEA
* VS Code

---

## 📂 Project Structure

```
daily-performance-tracker/
│
├── backend/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── entity/
│   └── config/
│
├── frontend/
│   ├── pages/
│   ├── components/
│   ├── apis/
│   └── styles/
│
└── README.md
```

---

## 🔌 API Endpoints (Focus Session)

### Start Focus Session

```
POST /api/v1/tasks/{taskId}/focus-sessions/start
```

### Stop Focus Session

```
POST /api/v1/tasks/{taskId}/focus-sessions/{sessionId}/stop
Body:
{
  "action": "COMPLETE" | "CANCEL"
}
```

### Get Sessions by Task

```
GET /api/v1/tasks/{taskId}/focus-sessions
```

---

## ⚙️ Setup & Run Locally

### 1️⃣ Clone repository

```bash
git clone https://github.com/YOUR_USERNAME/daily-performance-tracker.git
cd daily-performance-tracker
```

---

### 2️⃣ Run Backend

```bash
cd backend
./mvnw spring-boot:run
```

Server runs at:

```
http://localhost:8080
```

---

### 3️⃣ Run Frontend

```bash
cd frontend
npm install
npm start
```

Frontend runs at:

```
http://localhost:3000
```

---

## 🧠 Design Decisions

* Focus session duration is calculated on backend to prevent manipulation.
* Frontend timer acts only as UI feedback.
* Sessions persist independently from UI lifecycle.
* DTO mapping isolates database entities from API responses.

---


## 🔮 Future Improvements

* Auto resume running session after page reload
* JWT authentication & user accounts
* Analytics dashboard (charts)
* Mobile responsive optimization
* Background timer sync using WebSocket


