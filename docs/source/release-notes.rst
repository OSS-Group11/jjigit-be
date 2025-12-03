Release Notes
=============

This document lists the release notes for the JJiGiT platform.
Since JJiGiT consists of multiple repositories (Backend, Frontend, AI), we use a **Platform Version** to synchronize releases.

Platform Version 1.0.0 (MVP)
----------------------------

**Release Date**: January 2025

This is the first official MVP (Minimum Viable Product) release of JJiGiT.

Component Versions
~~~~~~~~~~~~~~~~~~

The Platform v1.0.0 consists of the following component versions:

* **Backend API**: v1.0.0
* **Frontend UI**: v1.0.0
* **AI Service**: v1.0.0

.. note::
   You can verify the source code for this release in the `releases` tag of each repository.

Key Highlights
~~~~~~~~~~~~~~

* **User Authentication**: Secure signup and login system using JWT.
* **Poll Creation**: Create public or private polls with multiple options.
* **Real-Time Voting**: "One Person, One Vote" system with instant result updates.
* **Visualization**: Interactive bar and pie charts for voting results.
* **Discussions**: Comment system with transparency badges showing voter choices.

Repository Specific Changes
~~~~~~~~~~~~~~~~~~~~~~~~~~~

Backend (v1.0.0)
^^^^^^^^^^^^^^^^

* **API**: Implemented RESTful endpoints for Auth, Polls, Votes, and Comments.
* **Database**: Designed MySQL schema with constraints for data integrity.
* **Security**: Applied Spring Security with JWT filter chain.
* **Docs**: Integrated Swagger/OpenAPI v3.

Frontend (v1.0.0)
^^^^^^^^^^^^^^^^^

* **UX/UI**: Responsive design using Tailwind CSS.
* **State Management**: Implemented React Hooks for managing auth and poll states.
* **Integration**: Connected to Backend API using Axios with interceptors.
* **Charts**: Integrated Chart.js for real-time data rendering.

AI Service (v1.0.0)
^^^^^^^^^^^^^^^^^^^

* **Setup**: Initialized Python environment with Flask/FastAPI.
* **Models**: Set up baseline structure for future LLaMA model integration.

Known Issues
~~~~~~~~~~~~

* **Mobile View**: Some charts may require horizontal scrolling on very small screens.
* **Password Reset**: Self-service password reset is not yet available.
* **Edit Function**: Polls and comments cannot be edited after submission in this version.

Upgrade Guide
~~~~~~~~~~~~~

To run this version locally:

1. Pull the ``main`` branch from all three repositories.
2. Ensure database schema is updated (``ddl-auto: update``).
3. Rebuild the Backend project (``./gradlew build``).
4. Re-install Frontend dependencies (``npm install``).
