# Domain Model

This document describes the core domain model of the Asset Monitoring system.

## Overview

- An **Asset** (e.g. a machine or piece of equipment) has a status and owns a list of **Sensor**s.
- A **Sensor** belongs to exactly one asset, has a type, and defines warning/critical thresholds.
- A **Measurement** is a single reading recorded by a sensor at a point in time.
- An **Alert** is raised against a sensor when a measurement breaches a threshold, and moves through a defined lifecycle (state machine).

## Class Diagram

```mermaid
classDiagram
    class Asset {
        -Long id
        -String code
        -String name
        -AssetStatus status
        -Instant createdAt
        -List~Sensor~ sensors
        +addSensor(Sensor)
        +decommission()
        +putUnderMaintenance()
        +activate()
        +rename(String)
    }

    class Sensor {
        -Long id
        -String serial
        -SensorType type
        -boolean active
        -BigDecimal warningThreshold
        -BigDecimal criticalThreshold
        -Asset asset
        +setThresholds(BigDecimal, BigDecimal)
        +severityOf(BigDecimal) AlertSeverity
        +isWithinLimits(BigDecimal) boolean
        +activate()
        +deactivate()
    }

    class Measurement {
        -Long id
        -Sensor sensor
        -BigDecimal value
        -String unit
        -Instant recordedAt
    }

    class Alert {
        -Long id
        -Sensor sensor
        -AlertState state
        -AlertSeverity severity
        -Instant raisedAt
        -Instant ackAt
        -Instant resolvedAt
        +escalate(AlertSeverity)
        +acknowledge()
        +resolve()
        +isOpen() boolean
    }

    class AssetStatus {
        <<enumeration>>
        ACTIVE
        MAINTENANCE
        DECOMMISSIONED
    }

    class SensorType {
        <<enumeration>>
        TEMPERATURE
        VIBRATION
        FLOW
        PRESSURE
        +defaultUnit() String
    }

    class AlertSeverity {
        <<enumeration>>
        NORMAL
        WARNING
        CRITICAL
        +isAbove(AlertSeverity) boolean
    }

    class AlertState {
        <<enumeration>>
        NORMAL
        WARNING
        CRITICAL
        ACKNOWLEDGEMENT
        RESOLVED
        +isValidTransition(AlertState) boolean
    }

    class IllegalAlertTransitionException {
        +IllegalAlertTransitionException(AlertState, AlertState)
    }

    Asset "1" o-- "0..*" Sensor : sensors
    Sensor "1" -- "0..*" Measurement : readings
    Sensor "1" -- "0..*" Alert : alerts
    Asset --> AssetStatus
    Sensor --> SensorType
    Alert --> AlertState
    Alert --> AlertSeverity
    Alert ..> IllegalAlertTransitionException : throws
```

## Alert State Machine

An `Alert` transitions between states via `Alert.transitionTo`, validated by `AlertState.isValidTransition`. An invalid transition throws `IllegalAlertTransitionException`.

```mermaid
stateDiagram-v2
    [*] --> WARNING : raise (severity = WARNING)
    [*] --> CRITICAL : raise (severity = CRITICAL)

    NORMAL --> WARNING
    NORMAL --> CRITICAL

    WARNING --> CRITICAL : escalate
    WARNING --> ACKNOWLEDGEMENT : acknowledge
    WARNING --> NORMAL

    CRITICAL --> ACKNOWLEDGEMENT : acknowledge

    ACKNOWLEDGEMENT --> RESOLVED : resolve

    RESOLVED --> NORMAL
```

## Notes

- `Asset.addSensor` also calls `Sensor.attachTo`, keeping the bidirectional Asset↔Sensor link consistent; `Asset.getSensors()` returns an immutable copy to protect encapsulation.
- `Asset.decommission()` cascades to deactivate all of its sensors.
- `Sensor.severityOf(value)` compares a reading against `criticalThreshold` first, then `warningThreshold`, defaulting to `NORMAL`.
- `Alert` cannot be constructed with `AlertSeverity.NORMAL` — an alert only exists once a breach has occurred.
- Equality: `Asset` by `code`, `Sensor` by `serial`, `Alert` by `id`.
