
# Online Book Store

Online Book Store is a web service that allows you to access and manage a collection of books. You can use the API to perform various operations on books, such as creating, updating, deleting, searching, and sorting. The API also provides information about the authors, genres, ratings, and reviews of the books. The Book Store API is designed to be simple, fast, and secure. It uses the REST-full architecture and JSON format for data exchange. The API supports authentication, pagination, filtering, and error handling.


## Content Table

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Controllers](#controllers)
- [Prerequisites](#prerequisites)
- [Run Locally](#run-locally)
- [Challenges](#challenges)
- [Conclusion](#conclusion)


## Features

#### User registration and login:
Users can create an account and log in using their email and password.

#### Book browsing and searching:
Users can browse and search for books by various criteria, such as title, author, genre, price etc. Users can also view the details of each book.

#### Book ordering and payment:
Users can add books to their shopping cart and after that can make order. Users can also track the status of their orders and view their order history.

#### Documentation and tests:
Users can access the documentation of the web application and the API on the official website. The documentation provides information about the features, functionalities, usage, and examples of the web application and the API. Users can also run tests on the web application and the API using various tools, such as Postman, Jest, Mocha, etc. The tests check the correctness, performance, security, and usability of the web application and the API.
## Tech Stack

**Spring Boot**: Spring Boot is a framework that simplifies the development and deployment of Spring-based applications.

**Spring Security**: Spring Security is a framework that provides authentication, authorization, and protection against common attacks for Spring-based applications.

**JWT (JSON Web Tokens)**: JWT (JSON Web Token) is an open standard (RFC 7519) that defines a compact and self-contained way for securely transmitting information between parties as a JSON object.

**Spring Data JPA**: Spring Data JPA is a module of the Spring Data project that provides repository support for the Java Persistence API (JPA).

**Hibernate**: Hibernate is an open source framework that implements the object-relational mapping (ORM) concept for Java applications.

**Liquibase**: Liquibase is an open source tool that manages database changes and migrations. It tracks the database schema changes in changelog files, which can be written in XML, JSON, YAML, or SQL formats.

**Swagger**: Swagger is a suite of tools for API development based on the OpenAPI Specification. It allows developers to design, document, test, and consume REST-full APIs in a visual and interactive way.


## Controllers

**AuthenticationController**: Handles authentication and registration.

**BookController**: Handles books operations: create, update, delete, get, search.

**CategoryController**: Handles categories operations: create, update, delete, get, get books by category.

**OrderController**: Handles order operations: create, get, update; handles order item get operations.

**ShoppingCartController**: Handles shopping cart get operation, and cart item operations: create, delete, update.


## Prerequisites

- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven(Not required if using Maven Wrapper)](https://maven.apache.org/download.cgi)
- [MySQL database(Not required if running with Docker)](https://www.mysql.com/downloads/)
- [Docker](https://docs.docker.com/get-docker/)


## Run Locally

Clone the project:

```bash
  git clone https://github.com/OlegTuryk/online-book-store.git
```

Build with Maven:

```bash
  mvn clean install
```

Docker Compose your project:

```bash
  docker compose build
  docker compose up
```


## Challenges

### Challenge 1: Implementing authentication and authorization

I had to ensure that only registered and logged-in users could access and manage their books, orders, etc. I also had to prevent unauthorized users from accessing or modifying other users’ data. To achieve this, I used Spring Security and JWT (JSON Web Tokens).

### Challenge 2: Managing database changes and migrations

I had to keep track of the changes made to the database schema and data throughout the development process. I also had to apply the changes to different environments, such as development, testing, and production. To facilitate this, I used Liquibase, which is a tool that manages database changes and migrations.

### Challenge 3: Documenting and testing the API

I had to document the API endpoints and errors for my web service. I also had to test the functionality and performance of the API using various scenarios and inputs. To simplify this task, I used Swagger, which is a suite of tools for API development based on the OpenAPI Specification.


## Conclusion

This project is a web application that allows users to browse and buy books online. It is built using the Spring Boot technologies. It uses technologies and tools, such as Spring Security, JWT, Spring Data JPA, Hibernate, Liquibase, and Swagger. The project demonstrates my skills and knowledge as a web developer, as well as my ability to overcome various challenges and learn new things. I hope you enjoy using this application and find it useful and fun. If you have any feedback, questions, or suggestions, please feel free to contact me. Thank you for your interest and support.

The video instructions for usage you can watch here:
[loom-video](https://www.loom.com/share/ff24d8ed07ed40c9957801b37251786c?sid=49ac1432-aaeb-480c-a048-3b915ea16472)
