# 01Blog

## Overview

**01Blog** is a social blogging platform designed for students to document and share their learning journey.

Users can create posts, upload media, follow other students, interact through likes and comments, and report inappropriate content. Administrators have moderation tools to manage users, posts, and reports.

The project is built as a **fullstack application** using:

- **Java Spring Boot** for the backend REST API
- **Angular** for the frontend interface
- **PostgreSQL/MySQL** for data storage

The goal of this project is to demonstrate fullstack development skills including backend architecture, frontend UI development, authentication, and secure content management.

---

# Learning Objectives

This project demonstrates the ability to:

- Build REST APIs using **Spring Boot**
- Implement **authentication and authorization with JWT**
- Design **relational databases**
- Manage **user-generated content**
- Develop **Angular applications**
- Implement **role-based access control**
- Build **admin moderation tools**
- Structure a **fullstack architecture**
- Collaborate using **Git and GitHub**

---

# Technologies Used

## Backend

- Java
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA
- Hibernate
- PostgreSQL / MySQL
- Maven

## Frontend

- Angular
- TypeScript
- Angular Material / Bootstrap
- RxJS
- HTML5
- CSS3

## Other Tools

- Git & GitHub
- REST APIs
- Multipart File Upload
- Media Storage (Local file system / Cloud storage)

---

# Backend Features

## Authentication

- User registration
- User login
- Secure password hashing
- JWT-based authentication
- Role-based access control (USER / ADMIN)

---

## User Block Page

Each user has a **public profile page ("block")** that displays:

- User information
- All posts created by the user
- Subscription options

Users can:

- Subscribe to other profiles
- Unsubscribe from profiles
- Receive notifications when subscribed users post content

---

## Posts

Users can:

- Create posts
- Edit posts
- Delete posts
- Upload **images or videos**
- Add descriptions to posts

Each post contains:

- Timestamp
- Media preview
- Description
- Likes
- Comments

Other users can:

- Like posts
- Comment on posts

---

## Reports

Users can report:

- Posts
- Comments
- User profiles

Each report includes:

- Reason
- Description
- Timestamp
- Reporter information

Reports are visible **only to administrators**.

---

## Admin Panel

Admins can:

- View all users
- View all posts
- View submitted reports
- Remove inappropriate posts
- Ban or delete users
- Moderate reported content

All admin routes are protected with **role-based access control**.

---

# Frontend Features

## User Experience

- Homepage displaying posts from **subscribed users**
- User profile page ("block")
- View other user profiles
- Subscribe / unsubscribe from users

---

## Post Interaction

Users can:

- Like posts
- Comment on posts
- Upload images/videos
- View media previews
- See timestamps, likes, and comments

---

## Notifications

- Notification icon in the navbar
- Shows updates from subscribed profiles
- Ability to mark notifications as **read/unread**

---

## Reporting

Users can report inappropriate content via:

- Modal UI component
- Confirmation dialog before submission
- Text description of the report reason

---

## Admin Dashboard

Admins can:

- View all users
- View all posts
- View all reports
- Ban users
- Delete posts
- Moderate content

The dashboard is built with a **clean and responsive interface**.

---

# Security

Security is implemented using:

- **Spring Security**
- **JWT Authentication**
- **Role-based access control**
- Secure password hashing
- Protected API routes
- Authorization checks for admin endpoints

---

# Database

The application uses a **relational SQL database**.

Example entities:

- User
- Post
- Comment
- Like
- Subscription
- Notification
- Report

Relationships include:

- Users create posts
- Users like posts
- Users comment on posts
- Users subscribe to other users
- Users create reports

---

# Media Storage

Posts support media uploads:

- Images
- Videos

Media files are stored securely in:

- Local file system  
or
- Cloud storage (e.g., AWS S3)

---

# Project Structure

## Backend Structure (Spring Boot)
```
.
├── admin
│   ├── controller
│   │   └── AdminController.java
│   ├── response
│   │   └── AdminDashboardResponse.java
│   └── service
│       └── AdminService.java
├── auth
│   ├── controller
│   │   └── AuthController.java
│   ├── dto
│   │   ├── UserLogin.java
│   │   └── UserRegister.java
│   ├── model
│   │   └── User.java
│   ├── repository
│   │   └── UserRepository.java
│   ├── response
│   │   └── UserResponse.java
│   └── service
│       └── AuthService.java
├── BackendApplication.java
├── common
│   ├── config
│   │   └── WebConfig.java
│   ├── DataSeeder.java
│   ├── exception
│   │   └── GlobalExceptionHandler.java
│   ├── response
│   │   └── ResponseData.java
│   └── validator
│       └── FileValidator.java
├── medias
│   ├── model
│   │   └── PostMedias.java
│   ├── repository
│   │   └── PostMediaRepository.java
│   ├── response
│   │   └── PostMediaResponse.java
│   └── service
│       └── PostMediaService.java
├── notification
│   ├── controller
│   │   └── NotificationController.java
│   ├── model
│   │   └── Notification.java
│   ├── repository
│   │   └── NotificationRepository.java
│   ├── response
│   │   └── NotificationResponse.java
│   └── service
│       └── NotificationService.java
├── post
│   ├── controller
│   │   ├── CommentController.java
│   │   ├── LikeController.java
│   │   └── PostController.java
│   ├── dto
│   │   ├── CommentRequest.java
│   │   └── PostRequest.java
│   ├── model
│   │   ├── Comment.java
│   │   ├── Like.java
│   │   └── Post.java
│   ├── repository
│   │   ├── CommentRepository.java
│   │   ├── LikeRepository.java
│   │   └── PostRepository.java
│   ├── response
│   │   ├── CommentResponse.java
│   │   ├── PostResponse.java
│   │   └── TopPostResponse.java
│   └── service
│       ├── CommentService.java
│       ├── LikeService.java
│       └── PostService.java
├── profile
│   ├── controller
│   │   └── ProfileController.java
│   ├── dto
│   │   └── EditProfileRequest.java
│   ├── response
│   │   └── ProfileResponse.java
│   └── service
│       └── ProfileService.java
├── report
│   ├── controller
│   │   └── ReportController.java
│   ├── dto
│   │   └── ReportRequest.java
│   ├── model
│   │   └── Report.java
│   ├── repository
│   │   └── ReportRepository.java
│   ├── response
│   │   └── ReportResponse.java
│   └── service
│       └── ReportService.java
├── search
│   ├── controller
│   │   └── SearchController.java
│   ├── response
│   │   └── SearchResponse.java
│   └── service
│       └── SearchService.java
├── security
│   ├── CustomUserDetailsService.java
│   ├── JwtAuthenticationFilter.java
│   ├── JwtUtils.java
│   └── SecurityConfig.java
└── subscribes
    ├── controller
    │   └── SubscribeController.java
    ├── model
    │   └── Subscribe.java
    ├── repository
    │   └── SubscribesRepository.java
    └── service
        └── SubscribeService.java

```
## Frontend Structure (Angular)
```
.
├── app.config.ts
├── app.css
├── app.html
├── app.routes.ts
├── app.spec.ts
├── app.ts
├── components
│   ├── about
│   │   ├── about.component.css
│   │   ├── about.component.html
│   │   └── about.component.ts
│   ├── add-post
│   │   ├── add-post.component.css
│   │   ├── add-post.component.html
│   │   └── add-post.component.ts
│   ├── admin
│   │   ├── admin-layout.component.css
│   │   ├── admin-layout.component.html
│   │   ├── admin-layout.component.ts
│   │   ├── admin-nav-bar
│   │   │   ├── admin-nav-bar.component.css
│   │   │   ├── admin-nav-bar.component.html
│   │   │   └── admin-nav-bar.component.ts
│   │   ├── admin.routes.ts
│   │   ├── dashboard
│   │   │   ├── admin-dashboard.component.css
│   │   │   ├── admin-dashboard.component.html
│   │   │   └── admin-dashboard.component.ts
│   │   └── report-details
│   │       ├── admin-report-details.component.css
│   │       ├── admin-report-details.component.html
│   │       └── admin-report-details.component.ts
│   ├── comment
│   │   ├── comment.component.css
│   │   ├── comment.component.html
│   │   └── comment.component.ts
│   ├── home
│   │   ├── home.component.css
│   │   ├── home.component.html
│   │   └── home.component.ts
│   ├── nav-bar
│   │   ├── nav-bar.component.css
│   │   ├── nav-bar.component.html
│   │   └── nav-bar.component.ts
│   ├── notifications
│   │   ├── notifications.component.css
│   │   ├── notifications.component.html
│   │   └── notifications.component.ts
│   ├── post
│   │   ├── post.component.css
│   │   ├── post.component.html
│   │   └── post.component.ts
│   ├── post-details
│   │   ├── post-details.component.css
│   │   ├── post-details.component.html
│   │   └── post-details.component.ts
│   ├── profile
│   │   ├── profile.component.css
│   │   ├── profile.component.html
│   │   └── profile.component.ts
│   ├── report-dialog
│   │   ├── report-dialog.component.css
│   │   ├── report-dialog.component.html
│   │   └── report-dialogcomponent.ts
│   ├── search
│   │   ├── search.component.css
│   │   ├── search.component.html
│   │   └── search.component.ts
│   └── toast
│       ├── toast.component.css
│       ├── toast.component.html
│       └── toast.component.ts
├── guards
│   ├── admin-guard.ts
│   ├── auth-guard.spec.ts
│   ├── auth-guard.ts
│   └── guest-guard.ts
├── interceptors
│   └── auth.interceptor.ts
├── models
│   ├── admin-dashboard-response.model.ts
│   ├── api-response.model.ts
│   ├── comment-request.model.ts
│   ├── comment-response.model.ts
│   ├── media-preview.model.ts
│   ├── notification-response.model.ts
│   ├── notification-type.enum.ts
│   ├── post-media-response.model.ts
│   ├── post-request.model.ts
│   ├── post-response.model.ts
│   ├── profile-response.model.ts
│   ├── report-request.model.ts
│   ├── report-response.model.ts
│   ├── report-target.enum.ts
│   ├── user-response.model.ts
│   └── user-role.model.ts
├── pages
│   ├── homePage
│   │   ├── app.router.ts
│   │   ├── homePage.css
│   │   ├── homePage.html
│   │   └── homePage.ts
│   ├── login
│   │   ├── login.css
│   │   ├── login.html
│   │   └── login.ts
│   └── register
│       ├── register.css
│       ├── register.html
│       └── register.ts
└── services
    ├── admin.service.ts
    ├── auth.service.ts
    ├── comment.service.ts
    ├── like.service.ts
    ├── notification.service.ts
    ├── post.service.ts
    ├── profile.service.ts
    ├── report.service.ts
    ├── search.service.ts
    ├── subscribe.service.ts
    ├── toast.service.ts
    └── utils.service.ts
```