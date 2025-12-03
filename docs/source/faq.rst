Frequently Asked Questions
==========================

General Questions
-----------------

What is JJiGiT?
~~~~~~~~~~~~~~~

JJiGiT is an open-source, real-time polling platform. It allows users to create polls, vote instantly, and discuss topics through comments. It is designed to be simple, transparent, and free to use.

Is JJiGiT free?
~~~~~~~~~~~~~~~

Yes, JJiGiT is 100% free and open-source. There are no ads or paid features.

Do I need to log in to use JJiGiT?
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

You can view public polls and see voting results without logging in. However, you must log in to:

* Create a new poll
* Vote on a poll
* Post a comment

Account & Security
------------------

What information is required to sign up?
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

We only require a **username** and **password**. We do not collect email addresses, phone numbers, or any other personal information.

What happens if I forget my password?
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Since we do not collect email addresses, we cannot verify your identity to reset your password. If you lose your password, you will need to create a new account.

Is my password safe?
~~~~~~~~~~~~~~~~~~~~

Yes. Passwords are encrypted using **BCrypt** before being stored in our database. We never store passwords in plain text.

Poll Management
---------------

What is the difference between Public and Private polls?
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

* **Public Polls**: These appear in the main poll list and are visible to everyone. Anyone can find them and vote.
* **Private Polls**: These are **hidden** from the main list. Only people who have the direct link (URL) can access and vote on them.

Can I edit a poll after creating it?
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

No. To ensure the integrity of the voting process, polls cannot be modified once created. If you made a mistake, please delete the poll and create a new one.

Can I delete my poll?
~~~~~~~~~~~~~~~~~~~~~

Yes, if you are the creator of the poll, you can delete it. This action is permanent and cannot be undone.

How many options can I add?
~~~~~~~~~~~~~~~~~~~~~~~~~~~

A poll must have at least **2 options**. There is no specific limit on the maximum number of options in the current version.

Voting
------

Can I vote more than once?
~~~~~~~~~~~~~~~~~~~~~~~~~~

No. JJiGiT enforces a strict **"One Person, One Vote"** policy. Once you have voted on a poll, you cannot vote again using the same account.

Can I change or cancel my vote?
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

No. Once a vote is submitted, it is final. You cannot change your choice or withdraw your vote.

Do I have to vote to see the results?
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

No. You can view the real-time results (graphs and percentages) at any time, even if you haven't voted yet.

Comments
--------

Who can see my comments?
~~~~~~~~~~~~~~~~~~~~~~~~

Comments are public. Anyone who visits the poll page can see the comments.

What is the icon next to my comment?
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

To promote transparent discussion, your comment displays **which option you voted for** in that poll. This helps provide context to your opinion.

Can I edit or delete my comment?
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

In the current version, comments cannot be edited or deleted once posted. Please write carefully before submitting.

Can I reply to a specific comment (nested replies)?
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

No, currently only top-level comments are supported. All comments are displayed in a single list, sorted by date (Newest or Oldest).
