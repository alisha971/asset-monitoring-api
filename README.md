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