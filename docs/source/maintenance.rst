Maintenance and Troubleshooting
================================

This guide covers routine maintenance tasks and common troubleshooting scenarios for JJiGiT.

Regular Maintenance Tasks
--------------------------

Database Maintenance
~~~~~~~~~~~~~~~~~~~~

Backup Database
^^^^^^^^^^^^^^^

Regular backups are essential. Create automated backups:

.. code-block:: bash

   # Daily backup script
   mysqldump -u jjigit_user -p jjigit > backup_$(date +%Y%m%d).sql

   # Compressed backup
   mysqldump -u jjigit_user -p jjigit | gzip > backup_$(date +%Y%m%d).sql.gz

Restore from Backup
^^^^^^^^^^^^^^^^^^^

.. code-block:: bash

   # Restore from SQL file
   mysql -u jjigit_user -p jjigit < backup_20251203.sql

   # Restore from compressed file
   gunzip < backup_20251203.sql.gz | mysql -u jjigit_user -p jjigit

Optimize Tables
^^^^^^^^^^^^^^^

Periodically optimize database tables:

.. code-block:: sql

   USE jjigit;
   OPTIMIZE TABLE user;
   OPTIMIZE TABLE poll;
   OPTIMIZE TABLE vote;
   OPTIMIZE TABLE comment;
   OPTIMIZE TABLE option;

Check Database Size
^^^^^^^^^^^^^^^^^^^

.. code-block:: sql

   SELECT
       table_name AS `Table`,
       round(((data_length + index_length) / 1024 / 1024), 2) AS `Size (MB)`
   FROM information_schema.TABLES
   WHERE table_schema = "jjigit"
   ORDER BY (data_length + index_length) DESC;

Log Management
~~~~~~~~~~~~~~

Rotate Logs
^^^^^^^^^^^

Configure log rotation to prevent disk space issues:

.. code-block:: bash

   # /etc/logrotate.d/jjigit
   /var/log/jjigit/*.log {
       daily
       rotate 30
       compress
       delaycompress
       notifempty
       create 0640 jjigit jjigit
       sharedscripts
       postrotate
           systemctl reload jjigit
       endscript
   }

Clean Old Logs
^^^^^^^^^^^^^^

.. code-block:: bash

   # Delete logs older than 30 days
   find /var/log/jjigit -name "*.log" -mtime +30 -delete

Monitor Log Size
^^^^^^^^^^^^^^^^

.. code-block:: bash

   du -sh /var/log/jjigit/
   du -h /var/log/jjigit/*.log | sort -h

Application Updates
~~~~~~~~~~~~~~~~~~~

Update Dependencies
^^^^^^^^^^^^^^^^^^^

.. code-block:: bash

   # Check for outdated dependencies
   ./gradlew dependencyUpdates

   # Update Gradle wrapper
   ./gradlew wrapper --gradle-version=8.5

Update Application
^^^^^^^^^^^^^^^^^^

.. code-block:: bash

   # Pull latest changes
   git pull origin main

   # Rebuild application
   ./gradlew clean build

   # Restart service
   sudo systemctl restart jjigit

Security Updates
^^^^^^^^^^^^^^^^

* Regularly update Spring Boot and dependencies
* Monitor security advisories
* Apply critical patches immediately
* Review dependency vulnerability reports

Performance Monitoring
~~~~~~~~~~~~~~~~~~~~~~

Monitor Application Health
^^^^^^^^^^^^^^^^^^^^^^^^^^

.. code-block:: bash

   # Check health endpoint
   curl http://localhost:8080/actuator/health

   # Check metrics
   curl http://localhost:8080/actuator/metrics

Monitor Database Performance
^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. code-block:: sql

   -- Show slow queries
   SHOW PROCESSLIST;

   -- Check query cache hit rate
   SHOW STATUS LIKE 'Qcache%';

   -- View table locks
   SHOW OPEN TABLES WHERE In_use > 0;

Monitor Disk Space
^^^^^^^^^^^^^^^^^^

.. code-block:: bash

   # Check disk usage
   df -h

   # Check directory sizes
   du -sh /var/lib/mysql
   du -sh /var/log/jjigit

Common Issues and Solutions
----------------------------

Application Won't Start
~~~~~~~~~~~~~~~~~~~~~~~

Symptoms
^^^^^^^^

* Application fails to start
* Error: "Port already in use"
* Error: "Failed to connect to database"

Solutions
^^^^^^^^^

1. **Port Conflict**:

   .. code-block:: bash

      # Find process using port 8080
      lsof -ti:8080

      # Kill the process
      kill -9 <PID>

      # Or change port in application.yml

2. **Database Connection**:

   .. code-block:: bash

      # Check MySQL is running
      sudo systemctl status mysql

      # Start MySQL
      sudo systemctl start mysql

      # Verify credentials
      mysql -u jjigit_user -p

3. **Configuration Error**:

   * Check ``application.yml`` syntax
   * Verify environment variables are set
   * Check log files for specific errors

Database Connection Pool Exhausted
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Symptoms
^^^^^^^^

* Error: "Connection pool exhausted"
* Slow API responses
* Timeouts on database queries

Solutions
^^^^^^^^^

1. **Increase Pool Size**:

   .. code-block:: yaml

      spring:
        datasource:
          hikari:
            maximum-pool-size: 20
            minimum-idle: 10

2. **Check for Connection Leaks**:

   * Ensure all transactions are properly closed
   * Use ``@Transactional`` annotations correctly
   * Review long-running queries

3. **Monitor Connections**:

   .. code-block:: sql

      SHOW PROCESSLIST;
      SHOW STATUS LIKE 'Threads_connected';

Out of Memory Errors
~~~~~~~~~~~~~~~~~~~~

Symptoms
^^^^^^^^

* Error: "java.lang.OutOfMemoryError"
* Application crashes randomly
* Slow performance before crash

Solutions
^^^^^^^^^

1. **Increase Heap Size**:

   .. code-block:: bash

      java -Xms512m -Xmx2048m -jar jjigit-backend.jar

2. **Monitor Memory Usage**:

   .. code-block:: bash

      # JVM memory usage
      jmap -heap <PID>

      # Heap dump for analysis
      jmap -dump:format=b,file=heap.bin <PID>

3. **Check for Memory Leaks**:

   * Use profiling tools (VisualVM, JProfiler)
   * Review large object allocations
   * Check for unclosed resources

JWT Token Issues
~~~~~~~~~~~~~~~~

Symptoms
^^^^^^^^

* Error: "Invalid JWT token"
* Error: "JWT token expired"
* Authentication failures

Solutions
^^^^^^^^^

1. **Token Expired**:

   * Verify token expiration time
   * Implement token refresh mechanism
   * Check system clock synchronization

2. **Invalid Signature**:

   * Ensure JWT secret matches on all instances
   * Check for secret key changes
   * Verify token wasn't tampered with

3. **Malformed Token**:

   * Check token format: ``Bearer <token>``
   * Verify no extra spaces or characters
   * Ensure token is properly encoded

Slow API Performance
~~~~~~~~~~~~~~~~~~~~

Symptoms
^^^^^^^^

* API response times > 1 second
* Database queries taking too long
* High CPU usage

Solutions
^^^^^^^^^

1. **Database Query Optimization**:

   .. code-block:: sql

      -- Identify slow queries
      SHOW FULL PROCESSLIST;

      -- Enable slow query log
      SET GLOBAL slow_query_log = 'ON';
      SET GLOBAL long_query_time = 1;

2. **Add Database Indexes**:

   .. code-block:: sql

      -- Index frequently queried columns
      CREATE INDEX idx_poll_created_at ON poll(created_at);
      CREATE INDEX idx_vote_poll_id ON vote(poll_id);

3. **Enable JPA Query Optimization**:

   * Use ``@EntityGraph`` to avoid N+1 queries
   * Implement pagination for large result sets
   * Use projection queries when full entities aren't needed

4. **Enable Caching** (Future Enhancement):

   .. code-block:: yaml

      spring:
        cache:
          type: redis

CORS Errors
~~~~~~~~~~~

Symptoms
^^^^^^^^

* Browser console: "CORS policy blocked"
* Preflight requests failing
* Authentication not working from frontend

Solutions
^^^^^^^^^

1. **Add Frontend Origin**:

   Update ``SecurityConfig.java``:

   .. code-block:: java

      configuration.setAllowedOrigins(List.of(
          "http://localhost:3000",
          "https://your-frontend-domain.com"
      ));

2. **Enable Credentials**:

   .. code-block:: java

      configuration.setAllowCredentials(true);

3. **Check Allowed Methods**:

   .. code-block:: java

      configuration.setAllowedMethods(List.of(
          "GET", "POST", "PUT", "DELETE", "OPTIONS"
      ));

Database Migration Errors
~~~~~~~~~~~~~~~~~~~~~~~~~~

Symptoms
^^^^^^^^

* Entity changes not reflected in database
* Error: "Column not found"
* Schema mismatch errors

Solutions
^^^^^^^^^

1. **For Development**:

   .. code-block:: yaml

      spring:
        jpa:
          hibernate:
            ddl-auto: update

2. **For Production**:

   Use migration tools like Flyway:

   .. code-block:: yaml

      spring:
        flyway:
          enabled: true
          locations: classpath:db/migration

3. **Manual Migration**:

   .. code-block:: sql

      -- Add new column
      ALTER TABLE poll ADD COLUMN description TEXT;

      -- Modify column
      ALTER TABLE vote MODIFY COLUMN voted_at DATETIME NOT NULL;

Duplicate Vote Errors
~~~~~~~~~~~~~~~~~~~~~

Symptoms
^^^^^^^^

* Error: "User has already voted"
* Constraint violation on (voterId, pollId)

Solutions
^^^^^^^^^

This is expected behavior (1-vote-per-poll rule). To handle gracefully:

1. **Check Vote Status Before Voting**:

   Call ``GET /api/polls/{pollId}/voted`` first

2. **Handle 409 Conflict**:

   Display user-friendly message: "You have already voted on this poll"

3. **Verify Database Constraint**:

   .. code-block:: sql

      SHOW CREATE TABLE vote;

      -- Should see: UNIQUE KEY (voter_id, poll_id)

Log Analysis
------------

Finding Errors
~~~~~~~~~~~~~~

.. code-block:: bash

   # Search for errors in logs
   grep "ERROR" /var/log/jjigit/application.log

   # Count error occurrences
   grep -c "ERROR" /var/log/jjigit/application.log

   # View recent errors with context
   grep -B 5 -A 10 "ERROR" /var/log/jjigit/application.log | tail -50

Analyzing API Performance
~~~~~~~~~~~~~~~~~~~~~~~~~

.. code-block:: bash

   # Find slow API calls (> 1000ms)
   grep "took.*[0-9]{4,}ms" /var/log/jjigit/application.log

   # Most frequent endpoints
   grep "GET\|POST\|PUT\|DELETE" /var/log/jjigit/application.log | \
     awk '{print $5}' | sort | uniq -c | sort -nr

Database Query Logs
~~~~~~~~~~~~~~~~~~~

.. code-block:: bash

   # View all SQL queries
   grep "Hibernate:" /var/log/jjigit/application.log

   # Find queries with parameters
   grep "binding parameter" /var/log/jjigit/application.log

Health Checks
-------------

Application Health
~~~~~~~~~~~~~~~~~~

.. code-block:: bash

   # Check application status
   systemctl status jjigit

   # Check if port is listening
   netstat -tuln | grep 8080

   # Test health endpoint
   curl -f http://localhost:8080/actuator/health || echo "Health check failed"

Database Health
~~~~~~~~~~~~~~~

.. code-block:: bash

   # Check MySQL status
   sudo systemctl status mysql

   # Verify connection
   mysql -u jjigit_user -p -e "SELECT 1;"

   # Check database size
   mysql -u jjigit_user -p -e "
     SELECT table_schema AS 'Database',
            ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS 'Size (MB)'
     FROM information_schema.tables
     WHERE table_schema = 'jjigit';
   "

System Health
~~~~~~~~~~~~~

.. code-block:: bash

   # CPU usage
   top -b -n 1 | grep "Cpu(s)"

   # Memory usage
   free -h

   # Disk space
   df -h

   # Network connectivity
   ping -c 3 google.com

Automated Monitoring
--------------------

Health Check Script
~~~~~~~~~~~~~~~~~~~

Create a monitoring script:

.. code-block:: bash

   #!/bin/bash

   # health_check.sh

   # Check application
   if curl -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
       echo "✓ Application is healthy"
   else
       echo "✗ Application health check failed"
       systemctl restart jjigit
   fi

   # Check database
   if mysql -u jjigit_user -pYOUR_PASSWORD -e "SELECT 1;" > /dev/null 2>&1; then
       echo "✓ Database is healthy"
   else
       echo "✗ Database connection failed"
   fi

   # Check disk space
   DISK_USAGE=$(df -h / | awk 'NR==2 {print $5}' | sed 's/%//')
   if [ $DISK_USAGE -gt 90 ]; then
       echo "✗ Disk usage is critical: ${DISK_USAGE}%"
   else
       echo "✓ Disk usage is normal: ${DISK_USAGE}%"
   fi

Schedule with Cron
~~~~~~~~~~~~~~~~~~

.. code-block:: bash

   # Run health check every 5 minutes
   */5 * * * * /path/to/health_check.sh >> /var/log/jjigit/health.log 2>&1

Backup and Recovery
-------------------

Automated Backups
~~~~~~~~~~~~~~~~~

Create a backup script:

.. code-block:: bash

   #!/bin/bash

   # backup.sh

   BACKUP_DIR="/backups/jjigit"
   DATE=$(date +%Y%m%d_%H%M%S)

   # Create backup directory
   mkdir -p $BACKUP_DIR

   # Database backup
   mysqldump -u jjigit_user -pYOUR_PASSWORD jjigit | \
     gzip > "$BACKUP_DIR/db_$DATE.sql.gz"

   # Application backup (if needed)
   tar -czf "$BACKUP_DIR/app_$DATE.tar.gz" /opt/jjigit

   # Delete backups older than 30 days
   find $BACKUP_DIR -name "*.gz" -mtime +30 -delete

   echo "Backup completed: $DATE"

Schedule Daily Backups
~~~~~~~~~~~~~~~~~~~~~~

.. code-block:: bash

   # Run backup daily at 2 AM
   0 2 * * * /path/to/backup.sh >> /var/log/jjigit/backup.log 2>&1

Disaster Recovery
~~~~~~~~~~~~~~~~~

In case of complete failure:

1. **Restore Database**:

   .. code-block:: bash

      # Stop application
      sudo systemctl stop jjigit

      # Drop and recreate database
      mysql -u root -p -e "DROP DATABASE jjigit; CREATE DATABASE jjigit;"

      # Restore from backup
      gunzip < /backups/jjigit/db_latest.sql.gz | \
        mysql -u jjigit_user -p jjigit

2. **Redeploy Application**:

   .. code-block:: bash

      # Pull latest code
      cd /opt/jjigit
      git pull origin main

      # Rebuild
      ./gradlew clean build

      # Restart
      sudo systemctl start jjigit

3. **Verify Recovery**:

   .. code-block:: bash

      curl http://localhost:8080/actuator/health
      curl http://localhost:8080/api/polls

Getting Help
------------

When reporting issues, include:

* Application version
* Error messages from logs
* Steps to reproduce
* Environment (dev/prod)
* Recent changes

Where to Get Help:

* GitHub Issues: https://github.com/OSS-Group11/jjigit-be/issues
* Documentation: :doc:`index`
* API Reference: :doc:`api-reference`
* Configuration Guide: :doc:`configuration`
