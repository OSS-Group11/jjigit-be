Technical Overview
==================

This document outlines the architecture, technology stack, and database structure of JJiGiT.

System Architecture
-------------------

JJiGiT follows a client-server architecture using RESTful APIs.

* **Frontend**: Single Page Application (SPA) built with React.
* **Backend**: REST API server built with Spring Boot.
* **Database**: Relational data storage using MySQL.

Technology Stack
----------------

Backend
~~~~~~~

* **Framework**: Spring Boot 3.4.2
* **Language**: Java 21
* **Database**: MySQL 8.0+
* **ORM**: Spring Data JPA
* **Authentication**: JWT (JSON Web Token)
* **Build Tool**: Gradle
* **Documentation**: Swagger / SpringDoc OpenAPI

Frontend
~~~~~~~~

* **Library**: React 18+
* **Language**: JavaScript
* **Styling**: Tailwind CSS
* **Charts**: Chart.js (Data Visualization)
* **HTTP Client**: Axios

Domain Model
------------

Key entities and their relationships:

* **User**: Platform users (ID, Password, Username).
* **Poll**: Poll topics (Title, Public/Private status).
* **Option**: Poll choices (Text, Order).
* **Vote**: Voting records (Voter, Poll, Option) - *Enforces uniqueness*.
* **Comment**: User discussions (Author, Content, Timestamp).

Security
--------

* **Password Encryption**: BCrypt hashing algorithm.
* **API Security**: Stateless JWT authentication.
* **CORS**: Configured to allow requests only from trusted origins.
