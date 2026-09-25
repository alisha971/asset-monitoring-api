# Industrial Asset Monitoring API

A Spring Boot REST API for monitoring industrial assets, sensors, measurements, and operational alerts.

## Project Overview

This project models an industrial asset monitoring system in which physical assets are equipped with sensors that continuously produce measurements.

The system is designed to:

- Manage industrial assets and their sensors
- Record sensor measurements
- Evaluate measurements against warning and critical thresholds
- Raise, escalate, acknowledge, and resolve alerts when a measurement breaches a threshold
- Expose the monitoring system through a versioned REST API

## Technology Stack

- Java 25
- Spring Boot 4.1.1
- Maven
- Spring Web
- Jakarta Bean Validation
- Spring Boot Actuator

`spring-boot-starter-data-jpa` and a PostgreSQL driver are on the classpath for
a future persistence layer, but the application currently runs entirely
in-memory (see [Architecture](#architecture)) — no database is required to run it.

## Architecture

```text
Controller   — HTTP concerns only: parse requests, call services, map results to responses
    ↓
Service      — orchestrates use cases, enforces workflow, translates domain ↔ DTO
    ↓
Repository   — persistence abstraction; services depend on interfaces, not storage details
    ↓
Domain       — plain Java objects that own their own invariants and state transitions
```

Data currently lives in-memory (`ConcurrentHashMap`-backed repository
implementations), swappable for a real database behind the same repository
interfaces without touching the service layer.

Further documentation:

- [docs/domain_model.md](docs/domain_model.md) — the four core domain classes, their relationships, and the Alert state machine.
- [docs/repository_layer.md](docs/repository_layer.md) — the persistence abstraction and how it's implemented today.
- [docs/api-sequence-diagrams.md](docs/api-sequence-diagrams.md) — a sequence diagram for every REST endpoint, including failure paths.
- [docs/service-flows.md](docs/service-flows.md) — a flowchart for every service method.

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

The application exposes RESTful endpoints for managing assets, sensors, measurements, and alerts, all versioned under `/api/v1/assets`.

| Resource | Endpoints |
|---|---|
| Assets | list / get / create / rename / decommission / delete; list an asset's measurements |
| Sensors | list for an asset / create / deactivate |
| Measurements | list for a sensor / record / get the latest for a sensor |
| Alerts | list for a sensor / get / acknowledge / resolve |

Alerts have no direct create endpoint — they're raised or escalated automatically
whenever a recorded measurement breaches a sensor's warning/critical threshold.
Full request/response detail for every endpoint, including failure paths, is in
[docs/api-sequence-diagrams.md](docs/api-sequence-diagrams.md).

### Error Handling

A global `@RestControllerAdvice` maps every domain/service exception and bean
validation failure to a proper HTTP status (404 for not-found, 409 for
duplicates/conflicting state, 400 for invalid input, including field-level
messages for request-body validation) and a consistent JSON error body:

```json
{
  "timestamp": "2026-09-24T10:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Asset with ID: 9999 not found.",
  "path": "/api/v1/assets/9999",
  "fieldErrors": null
}
```

## Running the Application

### Prerequisites

* Java 25
* Maven

### Start the Application

```bash
mvn spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

