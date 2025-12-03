Configuration Guide
===================

This guide covers all configuration options for the JJiGiT backend.

Configuration Files
-------------------

JJiGiT uses Spring Boot's externalized configuration system with YAML files.

Configuration File Structure
~~~~~~~~~~~~~~~~~~~~~~~~~~~~

.. code-block:: text

   src/main/resources/
   ├── application.yml           # Common configuration
   ├── application-dev.yml       # Development environment
   └── application-prod.yml      # Production environment

Active Profile Selection
~~~~~~~~~~~~~~~~~~~~~~~~

Set the active profile via environment variable or command line:

**Environment Variable**

.. code-block:: bash

   export SPRING_PROFILES_ACTIVE=dev

**Command Line**

.. code-block:: bash

   ./gradlew bootRun --args='--spring.profiles.active=dev'

**Application Properties**

.. code-block:: yaml

   spring:
     profiles:
       active: dev

Database Configuration
----------------------

MySQL Connection
~~~~~~~~~~~~~~~~

Configure database connection in ``application-dev.yml`` or ``application-prod.yml``:

.. code-block:: yaml

   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/jjigit?useSSL=false&serverTimezone=UTC
       username: jjigit_user
       password: your_password
       driver-class-name: com.mysql.cj.jdbc.Driver

Connection Pool Settings
~~~~~~~~~~~~~~~~~~~~~~~~

JJiGiT uses HikariCP for connection pooling:

.. code-block:: yaml

   spring:
     datasource:
       hikari:
         maximum-pool-size: 10
         minimum-idle: 5
         connection-timeout: 30000
         idle-timeout: 600000
         max-lifetime: 1800000

**Configuration Options**:

* ``maximum-pool-size``: Maximum number of connections (default: 10)
* ``minimum-idle``: Minimum idle connections (default: 5)
* ``connection-timeout``: Max wait time for connection in milliseconds (default: 30000)
* ``idle-timeout``: Max idle time before connection is closed (default: 600000)
* ``max-lifetime``: Maximum lifetime of a connection (default: 1800000)

JPA Configuration
~~~~~~~~~~~~~~~~~

.. code-block:: yaml

   spring:
     jpa:
       hibernate:
         ddl-auto: update  # Options: none, validate, update, create, create-drop
       show-sql: true      # Log SQL statements
       properties:
         hibernate:
           format_sql: true
           use_sql_comments: true
           jdbc:
             batch_size: 20

**DDL Auto Options**:

* ``none``: No database schema generation
* ``validate``: Validate schema, no changes
* ``update``: Update schema if needed (development)
* ``create``: Create schema, drop existing data
* ``create-drop``: Create on startup, drop on shutdown

**Recommended Settings**:

* **Development**: ``ddl-auto: update``
* **Production**: ``ddl-auto: validate`` (use migration tools like Flyway)

JWT Configuration
-----------------

JWT Token Settings
~~~~~~~~~~~~~~~~~~

Configure JWT authentication in ``application.yml``:

.. code-block:: yaml

   jwt:
     secret: your-secret-key-here-must-be-at-least-256-bits
     expiration: 86400000  # 24 hours in milliseconds

**Security Best Practices**:

* Use a strong, randomly generated secret key (minimum 256 bits)
* Store secrets in environment variables, never commit to Git
* Rotate secrets periodically
* Use different secrets for dev and production

Using Environment Variables
~~~~~~~~~~~~~~~~~~~~~~~~~~~

.. code-block:: yaml

   jwt:
     secret: ${JWT_SECRET:default-dev-secret-do-not-use-in-production}
     expiration: ${JWT_EXPIRATION:86400000}

Set environment variables:

.. code-block:: bash

   export JWT_SECRET="your-production-secret-key"
   export JWT_EXPIRATION=86400000

Token Expiration Times
~~~~~~~~~~~~~~~~~~~~~~

Common expiration configurations:

* **1 hour**: 3600000 ms
* **24 hours**: 86400000 ms
* **7 days**: 604800000 ms
* **30 days**: 2592000000 ms

CORS Configuration
------------------

Cross-Origin Resource Sharing Settings
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

CORS is configured programmatically in ``SecurityConfig.java``:

.. code-block:: java

   configuration.setAllowedOrigins(List.of(
       "http://localhost:3000",
       "http://127.0.0.1:3000",
       "http://localhost:8080",
       "http://your-production-domain.com"
   ));

   configuration.setAllowedMethods(List.of(
       "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
   ));

   configuration.setAllowedHeaders(List.of("*"));
   configuration.setAllowCredentials(true);
   configuration.setMaxAge(3600L);

**Configuration Options**:

* ``allowedOrigins``: List of allowed origin URLs
* ``allowedMethods``: HTTP methods to allow
* ``allowedHeaders``: Headers that can be sent
* ``allowCredentials``: Allow cookies and authorization headers
* ``maxAge``: Preflight cache duration in seconds

Development CORS
~~~~~~~~~~~~~~~~

For local development, allow localhost:

.. code-block:: java

   "http://localhost:3000",      // React dev server
   "http://localhost:5173",      // Vite dev server
   "http://127.0.0.1:3000"

Production CORS
~~~~~~~~~~~~~~~

For production, specify exact domains:

.. code-block:: java

   "https://jjigit.com",
   "https://www.jjigit.com",
   "https://app.jjigit.com"

Never use ``*`` (wildcard) in production!

Server Configuration
--------------------

Port and Context Path
~~~~~~~~~~~~~~~~~~~~~

.. code-block:: yaml

   server:
     port: 8080
     servlet:
       context-path: /

Change port if 8080 is in use:

.. code-block:: yaml

   server:
     port: 8081

Compression
~~~~~~~~~~~

Enable response compression for better performance:

.. code-block:: yaml

   server:
     compression:
       enabled: true
       mime-types: application/json,application/xml,text/html,text/xml,text/plain

SSL/TLS Configuration
~~~~~~~~~~~~~~~~~~~~~

For HTTPS in production:

.. code-block:: yaml

   server:
     port: 8443
     ssl:
       enabled: true
       key-store: classpath:keystore.p12
       key-store-password: your_keystore_password
       key-store-type: PKCS12
       key-alias: jjigit

Session Configuration
~~~~~~~~~~~~~~~~~~~~~

JJiGiT uses stateless authentication, but session settings are defined as:

.. code-block:: yaml

   spring:
     security:
       session:
         creation-policy: STATELESS

Logging Configuration
---------------------

Log Levels
~~~~~~~~~~

Configure logging in ``application.yml``:

.. code-block:: yaml

   logging:
     level:
       root: INFO
       com.jigit.backend: DEBUG
       org.springframework.web: DEBUG
       org.hibernate.SQL: DEBUG
       org.hibernate.type.descriptor.sql.BasicBinder: TRACE

**Log Levels** (from most to least verbose):

* ``TRACE``: Very detailed logs
* ``DEBUG``: Debugging information
* ``INFO``: General information
* ``WARN``: Warning messages
* ``ERROR``: Error messages

Log File Configuration
~~~~~~~~~~~~~~~~~~~~~~

.. code-block:: yaml

   logging:
     file:
       name: logs/jjigit.log
       max-size: 10MB
       max-history: 30
     pattern:
       console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
       file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"

Production Logging
~~~~~~~~~~~~~~~~~~

For production, use more restrictive logging:

.. code-block:: yaml

   logging:
     level:
       root: WARN
       com.jigit.backend: INFO
       org.springframework: WARN
     file:
       name: /var/log/jjigit/application.log

Swagger/OpenAPI Configuration
-----------------------------

Swagger UI Settings
~~~~~~~~~~~~~~~~~~~

Swagger is configured in ``SwaggerConfig.java``:

.. code-block:: java

   @Bean
   public OpenAPI customOpenAPI() {
       return new OpenAPI()
           .info(new Info()
               .title("JJiGiT API")
               .version("1.0.0")
               .description("Real-time polling platform API")
               .license(new License().name("Apache 2.0"))
           );
   }

Disable Swagger in Production
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

To disable Swagger in production:

.. code-block:: yaml

   springdoc:
     api-docs:
       enabled: false
     swagger-ui:
       enabled: false

Or use conditional configuration:

.. code-block:: java

   @Profile("!prod")
   @Configuration
   public class SwaggerConfig {
       // ...
   }

Actuator Configuration
----------------------

Spring Boot Actuator Endpoints
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Enable health checks and monitoring:

.. code-block:: yaml

   management:
     endpoints:
       web:
         exposure:
           include: health,info,metrics
     endpoint:
       health:
         show-details: when-authorized

**Available Endpoints**:

* ``/actuator/health``: Application health status
* ``/actuator/info``: Application information
* ``/actuator/metrics``: Application metrics

Security for Actuator
~~~~~~~~~~~~~~~~~~~~~

Restrict actuator endpoints in production:

.. code-block:: yaml

   management:
     endpoints:
       web:
         exposure:
           include: health
     endpoint:
       health:
         show-details: never

Email Configuration (Planned)
-----------------------------

For email notifications (planned feature):

.. code-block:: yaml

   spring:
     mail:
       host: smtp.gmail.com
       port: 587
       username: your-email@gmail.com
       password: your-app-password
       properties:
         mail:
           smtp:
             auth: true
             starttls:
               enable: true

File Upload Configuration (Planned)
-----------------------------------

For poll image uploads (planned):

.. code-block:: yaml

   spring:
     servlet:
       multipart:
         enabled: true
         max-file-size: 5MB
         max-request-size: 10MB

Environment-Specific Configuration
----------------------------------

Development Environment
~~~~~~~~~~~~~~~~~~~~~~~

``application-dev.yml``:

.. code-block:: yaml

   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/jjigit_dev
     jpa:
       hibernate:
         ddl-auto: update
       show-sql: true

   logging:
     level:
       com.jigit.backend: DEBUG

   jwt:
     secret: dev-secret-key-not-for-production
     expiration: 86400000

Production Environment
~~~~~~~~~~~~~~~~~~~~~~

``application-prod.yml``:

.. code-block:: yaml

   spring:
     datasource:
       url: ${DATABASE_URL}
       username: ${DATABASE_USERNAME}
       password: ${DATABASE_PASSWORD}
     jpa:
       hibernate:
         ddl-auto: validate
       show-sql: false

   logging:
     level:
       root: WARN
       com.jigit.backend: INFO

   jwt:
     secret: ${JWT_SECRET}
     expiration: 86400000

   server:
     port: 8080
     compression:
       enabled: true

Docker Configuration
--------------------

Dockerfile
~~~~~~~~~~

Create a ``Dockerfile`` for deployment:

.. code-block:: dockerfile

   FROM eclipse-temurin:21-jre-alpine

   WORKDIR /app

   COPY build/libs/jjigit-backend-0.1.0.jar app.jar

   EXPOSE 8080

   ENTRYPOINT ["java", "-jar", "app.jar"]

Docker Compose
~~~~~~~~~~~~~~

``docker-compose.yml`` for local development:

.. code-block:: yaml

   version: '3.8'

   services:
     mysql:
       image: mysql:8.0
       environment:
         MYSQL_ROOT_PASSWORD: rootpass
         MYSQL_DATABASE: jjigit
         MYSQL_USER: jjigit_user
         MYSQL_PASSWORD: jjigit_pass
       ports:
         - "3306:3306"
       volumes:
         - mysql_data:/var/lib/mysql

     backend:
       build: .
       ports:
         - "8080:8080"
       environment:
         SPRING_PROFILES_ACTIVE: prod
         DATABASE_URL: jdbc:mysql://mysql:3306/jjigit
         DATABASE_USERNAME: jjigit_user
         DATABASE_PASSWORD: jjigit_pass
         JWT_SECRET: your-secret-key
       depends_on:
         - mysql

   volumes:
     mysql_data:

Configuration Best Practices
----------------------------

Security
~~~~~~~~

* Never commit secrets to version control.
* Use environment variables for sensitive data.
* Rotate secrets regularly.
* Use strong, randomly generated keys.
* Restrict CORS origins in production.

Performance
~~~~~~~~~~~

* Enable database connection pooling.
* Configure appropriate JPA batch sizes.
* Enable response compression.
* Use caching where appropriate.
* Monitor and tune GC settings.

Monitoring
~~~~~~~~~~

* Enable actuator endpoints for health checks.
* Configure structured logging.
* Set up log aggregation (ELK, CloudWatch).
* Monitor database connection pool.
* Track API response times.

Troubleshooting Configuration Issues
------------------------------------

Database Connection Failed
~~~~~~~~~~~~~~~~~~~~~~~~~~

Check:

* MySQL is running: ``sudo systemctl status mysql``
* Credentials are correct in ``application.yml``
* Database exists: ``mysql -u root -p -e "SHOW DATABASES;"``
* Firewall allows connection on port 3306

JWT Token Invalid
~~~~~~~~~~~~~~~~~

Check:

* Secret key matches between token generation and validation.
* Token hasn't expired.
* Clock skew isn't causing issues.
* Secret key is at least 256 bits.

CORS Errors
~~~~~~~~~~~

Check:

* Frontend origin is in the ``allowedOrigins`` list.
* ``allowCredentials`` is set to ``true``.
* Preflight requests (OPTIONS method) are handled.

Port Already in Use
~~~~~~~~~~~~~~~~~~~

Change the port:

.. code-block:: yaml

   server:
     port: 8081

Or find and kill the process using the port:

.. code-block:: bash

   # Linux/Mac
   lsof -ti:8080 | xargs kill -9

   # Windows
   netstat -ano | findstr :8080
   taskkill /PID <PID> /F

Configuration Validation
------------------------

Validate your configuration on startup by enabling fail-fast:

.. code-block:: yaml

   spring:
     config:
       fail-fast: true

This will cause the application to fail immediately if the configuration is invalid.

Additional Resources
--------------------

* `Spring Boot Configuration <https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config>`_
* `HikariCP Configuration <https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby>`_
* `JWT Best Practices <https://tools.ietf.org/html/rfc8725>`_
