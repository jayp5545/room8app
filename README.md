# Room8 App 

Room8 App is a comprehensive full-stack web application designed to simplify life for roommates. It facilitates collaborative management of expenses, announcements, tasks, grocery lists, and user profiles, helping roommates stay organized and share responsibilities efficiently.

Whether it's splitting bills, creating shopping lists, or assigning tasks, Room8 offers an intuitive platform to manage shared living in a streamlined way.

# Features

### Expense Management 
* **Add Expenses:** Navigate to the Expenses section and add a new expense. Specify details such as the amount, who paid, and which roommates should share the cost.
* **View Balances**: Check the dashboard to see how much each roommate owes or is owed.
### Announcements
* **Create Announcements:** Post updates or messages for your roommates to see. These could include reminders, events, or general notices

### Tasks & Grocery Lists
* **Assign Tasks:** Add tasks and assign them to roommates to ensure chores are evenly distributed.
* **Create Grocery Lists:** Make collaborative grocery lists, and update them as items are purchased.

### User Profiles
* **Customize Profiles:** Each roommate can update their profile with details like their name, contact information, and preferred communication methods.

# Technology Stack

### Frontend

* **Framework:** **Next.js** for server-side rendering and SEO-friendly web pages.
* **Styling:** **TailwindCSS** for utility-first, responsive design.
* **UI Components:** **ShadCN** for Components.

### Backend

* **Framework:** Spring Boot for building RESTful API's.
* **Database:** MySQL for relational data storage.
* **Testing:** JUnit and Mockito to ensure high test coverage and code quality.

### Deployment

* **Frontend:** Hosted on Vercel, optimized for high availability and responsiveness.
* **Backend:** Hosted on Render, integrated with the frontend and database.
* **DevOps:** The project follows Agile methodology with continuous integration and continuous deployment (CI/CD) pipelines.


# Architecture Overview


* **Frontend:** The Next.js-based frontend allows users to interact with the app through a responsive, intuitive interface.
* **Backend:** Spring Boot handles all the business logic and API endpoints, which the frontend consumes.
* **Database:** PostgreSQL stores data such as user information, expenses, tasks, and grocery lists.

# Setup

## Prerequisites

Before you start, ensure that you have the following installed:

* Node.js (version 14 or later) and npm (for running the frontend)
* Java (version 11 or later) and Maven (for running the backend)
* MySQL (for database setup)
* Docker (optional but recommended for containerized development)

## Step 1: Clone the repository

```bash
    git clone https://github.com/jayp5545/room8app.git
    cd room8app
```

## Step 2: Frontend Setup

```bash
    cd frontend
    pnpm install

```
Run the development server:
```bash
    npm run dev
```
## Step 3: Setup Database
Make a MySQL connection and a database and then add the credentials to application.properties file.
## Step 4: Backend Setup

```bash
    cd backend
    mvn clean install

```
Run the development server:
```bash
    mvn spring-boot:run
```


# Usage

## Expense Management

* **Add Expenses:** Navigate to the Expenses section and add a new expense. Specify details such as the amount, who paid, and which roommates should share the cost.
* **View Balances:** Check the dashboard to see how much each roommate owes or is owed.

## Announcements

* **Create Announcements:** Post updates or messages for your roommates to see. These could include reminders, events, or general notices.

## Tasks & Grocery Lists

* **Assign Tasks:** Add tasks and assign them to roommates to ensure chores are evenly distributed.
* **Create Grocery Lists:** Make collaborative grocery lists, and update them as items are purchased.

## User Profiles

* **Customize Profiles:** Each roommate can update their profile with details like their name.



