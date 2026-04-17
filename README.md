# JustEatTakeaway_Assessment

## Description

A Spring Boot REST API application for restaurant listing. This project provides endpoints to manage and retrieve restaurant information that can be integrated with a backend API.

## Prerequisites

- **Java 17** or higher
- **Maven 3.8** or higher

To verify installations:

```bash
java -version
mvn -version
```

## Project Structure

```
src/
├── main/
│   ├── java/com/example/restaurant/
│   │   └── Application.java          (Main Spring Boot application)
│   └── resources/
│       └── application.yml           (Configuration file)
└── test/
    └── java/com/example/restaurant/
```

## Building

Build the project using Maven:

```bash
mvn clean package
```

This generates an executable JAR file in the `target/` directory.

## Running

Run the application using one of these methods:

### Method 1: Maven (Development)

```bash
mvn spring-boot:run
```

### Method 2: Standalone JAR

```bash
java -jar target/restaurant-api-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080/home`

## API Endpoints

### Health & Info (Actuator)

- `GET /actuator/health` - Application health status
- `GET /actuator/info` - Application information

## Configuration

Application settings are defined in `src/main/resources/application.yml`:

- **Server Port**: 8080
- **Context Path**: /home
- **Logging Level**: INFO (root), DEBUG (com.example)
- **Actuator Endpoints**: health, info

## Technologies Used

- **Spring Boot**: 3.3.0
- **Java**: 17
- **Build Tool**: Maven
- **Configuration Format**: YAML
