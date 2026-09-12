# Industrial Asset Monitoring API

A Spring Boot REST API for monitoring industrial assets, sensors, measurements, and operational alerts.

## Project Overview

This project models an industrial asset monitoring system in which physical assets are equipped with sensors that continuously produce measurements.

The system is designed to:

- Manage industrial assets and their sensors
- Record sensor measurements
- Evaluate measurements against warning and critical thresholds
- Generate and manage alerts
- Expose the monitoring system through REST APIs
- Persist data using PostgreSQL

## Technology Stack

- Java 25
- Spring Boot 4.1.1
- Maven
- Spring Web
- Spring Data JPA
- PostgreSQL
- Jakarta Bean Validation
- Spring Boot Actuator

## Domain Model

The core domain consists of:

```text
Asset
  |
  └── Sensor
        |
        └── Measurement
        |
        └── Alert

```
### Asset

Represents a physical industrial asset such as a pump, motor, compressor, or other equipment being monitored.

### Sensor

Represents a sensor installed on an asset and responsible for producing measurements.

### Measurement

Represents a sensor reading recorded at a specific point in time.

### Alert

Represents an operational condition requiring attention, generated when sensor measurements exceed defined thresholds.

## API

The application exposes RESTful endpoints for managing assets, sensors, measurements, and alerts.

The API is versioned under:

```text
/api/v1
```

## Running the Application

### Prerequisites

* Java 25
* Maven
* PostgreSQL

### Start the Application

```bash
mvn spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

