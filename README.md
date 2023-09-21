# Cinema Reservation System
![Maven Build and Tests](https://github.com/Kafelixon/cinema-reservation/actions/workflows/maven.yml/badge.svg?event=push)

This repository contains the source code for a Cinema Reservation System built using Quarkus, JAX-RS, and PostgreSQL.

## Table of Contents

- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Database Setup](#database-setup)
- [Running the application in dev mode](#running-the-application-in-dev-mode)
- [Running Tests](#running-tests)
- [API Endpoints](#api-endpoints)
- [Packaging and running the application](#packaging-and-running-the-application)
- [Creating a native executable](#creating-a-native-executable)

## Getting Started

These instructions will help you get a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- JDK 11 or higher
- Maven
- PostgreSQL

### Database Setup

#### Step 1: Create the Database

Run this command in your terminal to create a new database named `JEE1`:

```shell script
createdb -e -E utf-8 JEE1
```

#### Step 2: Create the User

Execute the following command to create a new PostgreSQL user named `jee1` with the password `jee12345`:

```shell script
createuser -e -P jee1
```

When prompted, enter the password `jee12345`.

#### Step 3: Create the Schema

First, connect to the database:

```shell script
psql JEE1
```

Then, within the PostgreSQL prompt, execute:

```sql
CREATE SCHEMA jee1 AUTHORIZATION jee1;
```

#### Step 4: Connect to the Database

You can connect to the database using:

```shell script
psql JEE1 -U jee1 -h localhost
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```
Now You can open http://localhost:8080/ to interact with the app through a simple interface.

## Running Tests

If the application is running in dev mode, you can run the tests using:

http://localhost:8080/q/dev-ui/continuous-testing

Or you can run the tests using:

```shell script
./mvnw test
```

## API Endpoints

### Movies

- **GET /movies**: List all movies
  - Response: Array of movies
    ```json
    [
      {
        "id": 1,
        "name": "Movie 1",
        "duration": 120
      },
      {
        "id": 2,
        "name": "Movie 2",
        "duration": 140
      }
    ]
    ```
  
### Showtimes

- **GET /showtimes?movie_id={movie_id}**: List all showtimes for a specific movie
  - Query Parameter: `movie_id` (required)
  - Response: Array of showtimes
    ```json
    [
      {
        "id": 1,
        "hall_id": 1,
        "movie_id": 1,
        "show_time": "2023-09-10 15:00:00"
      }
    ]
    ```

### Reservations

- **POST /reservations/reserve**: Reserve a seat
  - Query Parameters: 
    - `showtimeId` (required)
    - `seatNumber` (required)
  - Response: Success message with reservation ID
    ```text
    Reservation successful! Reservation ID: 1
    ```

- **DELETE /reservations/cancel**: Cancel a reservation
  - Query Parameter: `reservationId` (required)
  - Response: Success message
    ```text
    Reservation canceled successfully!
    ```

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```
