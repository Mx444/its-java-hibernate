# Java Hibernate Exercises - ITS Steve Jobs Academy

This repository contains a Java project that uses Hibernate for connecting to and managing PostgreSQL databases, developed during the course at the ITS Steve Jobs Academy. The project covers various aspects of using Hibernate, from basic configuration to implementing database operations.

## Repository Structure

The repository is organized in the following structure:

- **src/main/java/com/example/**: Contains the Java classes of the project
  - Database connection utilities
  - Entity classes (models)
  - CRUD operations
  - Hibernate session management

- **src/main/resources/**: Configuration files
  - Hibernate configuration (hibernate.cfg.xml)
  - Property files

## Hibernate Configuration

The project uses a Hibernate configuration file to establish the connection with PostgreSQL:

```xml:src/main/resources/hibernate.cfg.xml
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE hibernate-configuration PUBLIC
    "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
    "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">

<hibernate-configuration>
    <session-factory>
        <!-- PostgreSQL Connection -->
        <property name="hibernate.connection.driver_class">org.postgresql.Driver</property>
        
        <!-- PostgreSQL Dialect -->
        <property name="hibernate.dialect">org.hibernate.dialect.PostgreSQLDialect</property>
        
        <!-- Connection Pool -->
        <property name="hibernate.c3p0.min_size">5</property>
        <property name="hibernate.c3p0.max_size">20</property>
        <property name="hibernate.c3p0.timeout">300</property>
        <property name="hibernate.c3p0.max_statements">50</property>
        <property name="hibernate.c3p0.idle_test_period">3000</property>
        
        <!-- Automatic table creation (use only in development) -->
        <property name="hibernate.hbm2ddl.auto">update</property>
        
        <!-- Query logging -->
        <property name="hibernate.show_sql">true</property>
        <property name="hibernate.format_sql">true</property>
    </session-factory>
</hibernate-configuration>
```

## Hibernate Utility

The `HibernateUtil` class provides methods for managing the database connection:

```java:src/main/java/com/example/HibernateUtil.java
package com.example;

import io.github.cdimascio.dotenv.Dotenv;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Dotenv dotenv = Dotenv.load();
            String dbHost = dotenv.get("DB_HOST");
            String dbPort = dotenv.get("DB_PORT");
            String dbName = dotenv.get("DB_NAME");
            String dbUser = dotenv.get("DB_USER");
            String dbPassword = dotenv.get("DB_PASSWORD");

            String jdbcUrl = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;

            return new Configuration()
                    .configure()
                    .setProperty("hibernate.connection.url", jdbcUrl)
                    .setProperty("hibernate.connection.username", dbUser)
                    .setProperty("hibernate.connection.password", dbPassword)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Error creating SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        getSessionFactory().close();
    }

    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        System.out.println("PostgreSQL connection successful!");
        session.close();
    }
}
```

## How to Use the Project

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Maven
- PostgreSQL
- `.env` file with the following variables:
  ```
  DB_HOST=localhost
  DB_PORT=5432
  DB_NAME=database_name
  DB_USER=username
  DB_PASSWORD=password
  ```

### Compilation and Execution
To compile the project, use the command:

```bash
mvn clean install
```

To run the connection test:

```bash
mvn exec:java -Dexec.mainClass="com.example.HibernateUtil"
```

## Operation Examples

### Creating a Session
```java
Session session = HibernateUtil.getSessionFactory().openSession();
```

### Executing a Transaction
```java
Session session = HibernateUtil.getSessionFactory().openSession();
Transaction tx = null;
try {
    tx = session.beginTransaction();
    // Database operations
    tx.commit();
} catch (Exception e) {
    if (tx != null) tx.rollback();
    e.printStackTrace();
} finally {
    session.close();
}
```

### CRUD Operations
```java
// Create
session.save(object);

// Read
Entity object = session.get(Entity.class, id);

// Update
session.update(object);

// Delete
session.delete(object);
```

## Educational Objectives

This project was designed to:

- Understand the fundamental concepts of Hibernate and JPA
- Develop skills in database connection management
- Apply CRUD operations in a practical context
- Improve the ability to write structured and object-oriented Java code

## Requirements

- JDK 8+
- Maven
- PostgreSQL
- Basic knowledge of Java and SQL

---

*This repository was created as part of the educational journey at ITS Steve Jobs Academy.*