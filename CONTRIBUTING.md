# Contributing to Jjigit

Thank you for your interest in contributing! Jjigit is an open-source backend service under the MIT License.  
This document describes the essential rules for contributing, including how to create Issues, submit Pull Requests, follow the branching model, and understand the maintainer review process.

Repository: **https://github.com/OSS-Group11/jjigit-be**

---

## 🧭 Code of Conduct

All contributors must follow the rules stated in **CODE_OF_CONDUCT.md**.  
Please refer to that file for full details.

---

## 📝 Creating an Issue

This repository already includes **Issue templates**.  
When creating an issue, please follow the template and include:

- A clear description of the bug or requested feature  
- Reproduction steps (for backend issues, include API details)  
- Request/response JSON or error logs  
- Environment information (e.g., Java version, DB version)

Issue types include:
- Bug Report  
- Feature Request  
- Documentation  
- Refactor  
- Discussion  

---

## 🔀 Branching Strategy

All development must take place in **separate branches** created from the main branch.

Branch naming conventions:
```
feature/<feature-name>
fix/<bug-name>
refactor/<topic>
```

Examples:
```
feature/create-vote-api
fix/invalid-token-bug
refactor/jpa-entity-structure
```

The **main branch is protected** — direct pushes are not allowed.  
All changes must go through a Pull Request.

---

## 🚀 Creating a Pull Request

A **PR template is already configured** in this repository.  
Please fill it out completely and include:

- Summary of changes  
- Reason for the change or the problem solved  
- Linked Issue (e.g., `Closes #12`)  
- Whether API or entity structures were affected  
- Description of how the changes were tested

Keep your PRs small and focused on a single purpose.

---

## 🔎 Maintainer Review Policy

All PRs require **approval from at least one Maintainer** before being merged.

A PR can be merged only if:

1. At least one Maintainer approves  
2. All GitHub Action checks pass  
3. There are no conflicts with main  
4. The PR template is fully completed  
5. API or logic changes are clearly explained so they can be reviewed

Maintainers verify code quality, structural consistency, and API stability before merging.

---

## 🛠 Development Setup

### Required environment

- Java 21  
- Spring Boot 3.4.x  
- Gradle (Groovy DSL)  
- MySQL 8+  
- Spring Data JPA  

### Run locally

```
./gradlew build
./gradlew bootRun
```

### Run tests

```
./gradlew test
```

---

## 📚 Documentation Contributions

Documentation contributions are welcome, including:

- Improvements to README  
- API documentation updates (SpringDoc / Swagger)  
- Development environment guides  
- Architecture or ERD documentation  

All documentation updates must also be submitted through a Pull Request.

---

## 🔒 Security

For security-related issues, **do not create a public Issue**.  
Instead, contact a Maintainer privately.

---

## ❤️ Thanks

All contributions — big or small — help improve Jjigit.  
Thank you for taking the time to contribute! 🚀
