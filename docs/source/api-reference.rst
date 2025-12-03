API Reference
=============

This document provides detailed API specifications for all implemented endpoints in JJiGiT.

Base URL
--------

.. code-block:: text

   http://localhost:8080/api

Authentication
--------------

Endpoints requiring security must include the Bearer Token in the request header.

.. code-block:: text

   Authorization: Bearer <your_jwt_token>

Authentication API
------------------

Sign Up
~~~~~~~

Register a new user. Passwords are encrypted before storage.

**Endpoint**

.. code-block:: text

   POST /api/auth/signup

**Request Body**

.. code-block:: json

   {
     "username": "user1234",
     "password": "password123!"
   }

**Success Response (200 OK)**

.. code-block:: json

   {
     "userId": 1
   }

**Error Responses**

* **409 Conflict**: Duplicate username

.. code-block:: json

   {
     "code": "DUPLICATE_USERNAME",
     "message": "Duplicate Username",
     "path": "/api/auth/signup",
     "timestamp": "2025-01-24T10:30:00"
   }

Log In
~~~~~~

Authenticate user and issue a JWT token.

**Endpoint**

.. code-block:: text

   POST /api/auth/login

**Request Body**

.. code-block:: json

   {
     "username": "user1234",
     "password": "password123!"
   }

**Success Response (200 OK)**

.. code-block:: json

   {
     "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     "userId": 1
   }

Validate Token
~~~~~~~~~~~~~~

Validate the validity of the JWT token held by the client.

**Endpoint**

.. code-block:: text

   GET /api/auth/validate

**Header**

* ``Authorization``: Bearer <token>

**Success Response (200 OK)**

.. code-block:: json

   {
     "isValid": true,
     "userId": 1
   }

Poll Management API
-------------------

Create Poll
~~~~~~~~~~~

Create a new poll. Requires at least 2 options.

**Endpoint**

.. code-block:: text

   POST /api/polls

**Authentication**: Required

**Request Body**

.. code-block:: json

   {
     "title": "Pineapple Pizza: Yes or No",
     "isPublic": true,
     "options": [
       { "optionText": "Yes", "optionOrder": 1 },
       { "optionText": "No", "optionOrder": 2 }
     ]
   }

**Success Response (200 OK)**

.. code-block:: json

   {
     "pollId": 1,
     "title": "Pineapple Pizza: Yes or No",
     "isPublic": true,
     "options": [
       {
         "optionId": 1,
         "optionText": "Yes",
         "optionOrder": 1,
         "voteCount": 0
       },
       {
         "optionId": 2,
         "optionText": "No",
         "optionOrder": 2,
         "voteCount": 0
       }
     ],
     "creatorId": 1,
     "createdAt": "2025-11-26T10:30:00"
   }

Get Public Polls
~~~~~~~~~~~~~~~~

Retrieve a paginated list of public polls.

**Endpoint**

.. code-block:: text

   GET /api/polls

**Query Parameters**

* ``page`` (int): Page number (default: 0)
* ``size`` (int): Items per page (default: 20)
* ``sort`` (string): Sort criteria (e.g., ``createdAt,desc``)

**Success Response (200 OK)**

.. code-block:: json

   {
     "polls": [
       {
         "pollId": 1,
         "title": "Pineapple Pizza: Yes or No",
         "isPublic": true,
         "options": [ ... ],
         "creatorId": 1,
         "createdAt": "2025-11-26T10:30:00",
         "totalVotes": 100
       }
     ],
     "currentPage": 0,
     "totalPages": 5,
     "totalElements": 50,
     "pageSize": 20
   }

Get Poll Detail
~~~~~~~~~~~~~~~

Retrieve detailed information for a specific poll.

**Endpoint**

.. code-block:: text

   GET /api/polls/{pollId}

**Path Parameters**

* ``pollId`` (long): Poll ID

**Success Response (200 OK)**

.. code-block:: json

   {
     "pollId": 1,
     "title": "Pineapple Pizza: Yes or No",
     "isPublic": true,
     "options": [
       {
         "optionId": 1,
         "optionText": "Yes",
         "optionOrder": 1,
         "voteCount": 42
       },
       {
         "optionId": 2,
         "optionText": "No",
         "optionOrder": 2,
         "voteCount": 58
       }
     ],
     "creatorId": 1,
     "createdAt": "2025-11-26T10:30:00",
     "totalVotes": 100
   }

Voting API
----------

Submit Vote
~~~~~~~~~~~

Vote for a specific option. (Limit: 1 vote per user per poll)

**Endpoint**

.. code-block:: text

   POST /api/polls/{pollId}/vote

**Authentication**: Required

**Request Body**

.. code-block:: json

   {
     "optionId": 1
   }

**Success Response (200 OK)**

.. code-block:: json

   {
     "message": "Vote recorded successfully",
     "voteId": 123,
     "pollId": 1,
     "optionId": 1,
     "votedAt": "2025-12-03T12:00:00"
   }

**Error Responses**

* **409 Conflict**: User has already voted on this poll.

Check Vote Status
~~~~~~~~~~~~~~~~~

Check if the current user has participated in this poll.

**Endpoint**

.. code-block:: text

   GET /api/polls/{pollId}/voted

**Authentication**: Required

**Success Response (200 OK)**

.. code-block:: json

   {
     "hasVoted": true,
     "votedOptionId": 1,
     "votedAt": "2025-12-03T12:00:00"
   }

Get Results
~~~~~~~~~~~

Retrieve aggregated poll results with vote counts and percentages. (No Auth required)

**Endpoint**

.. code-block:: text

   GET /api/polls/{pollId}/results

**Success Response (200 OK)**

.. code-block:: json

   {
     "pollId": 1,
     "totalVotes": 100,
     "options": [
       {
         "optionId": 1,
         "optionText": "Yes",
         "voteCount": 42,
         "percentage": 42.0
       },
       {
         "optionId": 2,
         "optionText": "No",
         "voteCount": 58,
         "percentage": 58.0
       }
     ]
   }

Comment API
-----------

Create Comment
~~~~~~~~~~~~~~

Post a comment on a poll.

**Endpoint**

.. code-block:: text

   POST /api/polls/{pollId}/comments

**Authentication**: Required

**Request Body**

.. code-block:: json

   {
     "content": "This poll is fun! I voted for option 1."
   }

**Success Response (200 OK)**

.. code-block:: json

   {
     "commentId": 1,
     "pollId": 1,
     "authorId": 5,
     "content": "This poll is fun! I voted for option 1.",
     "createdAt": "2025-12-03T12:05:00"
   }

Get Comments
~~~~~~~~~~~~

Retrieve all comments for a poll. Includes information about the option the author voted for.

**Endpoint**

.. code-block:: text

   GET /api/polls/{pollId}/comments

**Query Parameters**

* ``sortBy`` (string): Sort order (default: ``newest``)

  * ``newest``: Newest first
  * ``oldest``: Oldest first

**Success Response (200 OK)**

.. code-block:: json

   {
     "comments": [
       {
         "commentId": 1,
         "authorId": 5,
         "authorUsername": "user123",
         "content": "This poll is fun!",
         "createdAt": "2025-12-03T12:05:00",
         "votedOptionId": 1,
         "votedOptionText": "Yes"
       }
     ]
   }
