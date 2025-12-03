Getting Started
===============

This guide will help you set up and run JJiGiT on your local machine.

Prerequisites
-------------

Before you begin, ensure you have the following installed:

Required Software
~~~~~~~~~~~~~~~~~

* **Java 21 or higher**

  Download from `Oracle <https://www.oracle.com/java/technologies/downloads/>`_ or use OpenJDK

* **MySQL 8.0 or higher**

  Download from `MySQL Official Site <https://dev.mysql.com/downloads/mysql/>`_

* **Gradle** (optional, wrapper included)

  The project includes Gradle Wrapper, so manual installation is not required

* **Python 3.9+** (for Machine Learning features)

  Download from `Python.org <https://www.python.org/downloads/>`_

Verify Installation
~~~~~~~~~~~~~~~~~~~

Check that Java is properly installed:

.. code-block:: bash

   java --version

Check MySQL installation:

.. code-block:: bash

   mysql --version

Installation Steps
------------------

Step 1: Clone the Repository
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Clone the backend repository from GitHub:

.. code-block:: bash

   git clone https://github.com/OSS-Group11/jjigit-be.git
   cd jjigit-be

Step 2: Set Up MySQL Database
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Create a new MySQL database for JJiGiT:

.. code-block:: sql

   CREATE DATABASE jjigit;
   CREATE USER 'jjigit_user'@'localhost' IDENTIFIED BY 'your_password';
   GRANT ALL PRIVILEGES ON jjigit.* TO 'jjigit_user'@'localhost';
   FLUSH PRIVILEGES;

Step 3: Configure Application Properties
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Create or modify ``src/main/resources/application-dev.yml``:

.. code-block:: yaml

   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/jjigit?useSSL=false&serverTimezone=UTC
       username: jjigit_user
       password: your_password
       driver-class-name: com.mysql.cj.jdbc.Driver

     jpa:
       hibernate:
         ddl-auto: update
       show-sql: true
       properties:
         hibernate:
           format_sql: true

   jwt:
     secret: your-secret-key-here-change-in-production
     expiration: 86400000  # 24 hours in milliseconds

See :doc:`configuration` for detailed configuration options.

Step 4: Build the Project
~~~~~~~~~~~~~~~~~~~~~~~~~~

Build the project using Gradle Wrapper:

.. code-block:: bash

   ./gradlew build

On Windows, use:

.. code-block:: bash

   gradlew.bat build

Step 5: Run the Application
~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Start the Spring Boot application:

.. code-block:: bash

   ./gradlew bootRun

Or on Windows:

.. code-block:: bash

   gradlew.bat bootRun

The application will start on ``http://localhost:8080``

Step 6: Verify Installation
~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Check that the application is running:

.. code-block:: bash

   curl http://localhost:8080/actuator/health

Or visit the Swagger UI at:

.. code-block:: text

   http://localhost:8080/swagger-ui/index.html

Frontend Setup (Optional)
--------------------------

If you want to run the frontend as well:

Step 1: Clone Frontend Repository
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

.. code-block:: bash

   git clone https://github.com/OSS-Group11/jjigit-fe.git
   cd jjigit-fe

Step 2: Install Dependencies
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

.. code-block:: bash

   npm install

Step 3: Run Development Server
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

.. code-block:: bash

   npm start

The frontend will be available at ``http://localhost:3000``

Running Tests
-------------

Run all tests:

.. code-block:: bash

   ./gradlew test

Run specific test class:

.. code-block:: bash

   ./gradlew test --tests "com.jigit.backend.user.UserServiceTest"

View test reports at:

.. code-block:: text

   build/reports/tests/test/index.html

Common Issues
-------------

Port Already in Use
~~~~~~~~~~~~~~~~~~~

If port 8080 is already in use, change the port in ``application.yml``:

.. code-block:: yaml

   server:
     port: 8081

Database Connection Failed
~~~~~~~~~~~~~~~~~~~~~~~~~~

* Verify MySQL is running: ``sudo systemctl status mysql``
* Check credentials in ``application-dev.yml``
* Ensure the database ``jjigit`` exists

Build Failed
~~~~~~~~~~~~

* Clean the build: ``./gradlew clean build``
* Check Java version: ``java --version`` (must be 21+)
* Delete ``.gradle`` directory and rebuild

Next Steps
----------

Now that you have JJiGiT running locally:

1. Read :doc:`usage` to learn how to use the platform
2. Check :doc:`api-reference` to explore available APIs
3. See :doc:`contributing` if you want to contribute to the project

Quick Start Example
-------------------

Here's a quick example of creating your first poll using curl:

Step 1: Sign Up
~~~~~~~~~~~~~~~

.. code-block:: bash

   curl -X POST http://localhost:8080/api/auth/signup \
     -H "Content-Type: application/json" \
     -d '{
       "username": "testuser",
       "password": "password123"
     }'

Step 2: Log In
~~~~~~~~~~~~~~

.. code-block:: bash

   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{
       "username": "testuser",
       "password": "password123"
     }'

Save the returned JWT token.

Step 3: Create a Poll
~~~~~~~~~~~~~~~~~~~~~~

.. code-block:: bash

   curl -X POST http://localhost:8080/api/polls \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer YOUR_JWT_TOKEN" \
     -d '{
       "title": "What is your favorite programming language?",
       "isPublic": true,
       "options": [
         {"optionText": "Java", "optionOrder": 1},
         {"optionText": "Python", "optionOrder": 2},
         {"optionText": "JavaScript", "optionOrder": 3}
       ]
     }'

Step 4: Vote on the Poll
~~~~~~~~~~~~~~~~~~~~~~~~~

.. code-block:: bash

   curl -X POST http://localhost:8080/api/polls/{pollId}/vote \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer YOUR_JWT_TOKEN" \
     -d '{
       "optionId": 1
     }'

Step 5: View Results
~~~~~~~~~~~~~~~~~~~~

.. code-block:: bash

   curl http://localhost:8080/api/polls/{pollId}/results

Development Tips
----------------

Hot Reload
~~~~~~~~~~

For faster development, use Spring Boot DevTools (already included):

Changes to Java files will automatically reload the application.

Database Migrations
~~~~~~~~~~~~~~~~~~~

The application uses JPA's ``ddl-auto: update`` for development.

For production, consider using Flyway or Liquibase for versioned migrations.

Debugging
~~~~~~~~~

To run in debug mode:

.. code-block:: bash

   ./gradlew bootRun --debug-jvm

Then attach your IDE debugger to port 5005.

Additional Resources
--------------------

* `Spring Boot Documentation <https://docs.spring.io/spring-boot/docs/current/reference/html/>`_
* `MySQL Documentation <https://dev.mysql.com/doc/>`_
* `Gradle User Manual <https://docs.gradle.org/current/userguide/userguide.html>`_
* `JJiGiT GitHub Wiki <https://github.com/OSS-Group11/jjigit-be/wiki>`_
