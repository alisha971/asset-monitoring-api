# Repository Layer

This document describes the persistence abstraction used by the Asset Monitoring system.

## Overview

The repository layer provides an abstraction between the application and data storage.

The application interacts with repository interfaces rather than directly accessing the underlying storage implementation.

```text
AssetService
     |
     v
AssetRepository
     |
     v
InMemoryAssetRepository
     |
     v
ConcurrentHashMap
````

The same pattern is used for sensors and measurements.

## Repository Interfaces

### AssetRepository

Provides operations for:

* Saving an asset
* Finding an asset by ID
* Finding an asset by code
* Retrieving all assets
* Checking whether an asset code exists
* Deleting an asset by ID

### SensorRepository

Provides operations for:

* Saving a sensor
* Finding a sensor by ID
* Finding a sensor by serial number
* Finding sensors belonging to an asset
* Retrieving all sensors
* Deleting a sensor by ID

### MeasurementRepository

Provides operations for:

* Saving a measurement
* Finding a measurement by ID
* Finding measurements by sensor
* Finding measurements by asset
* Filtering measurements by sensor and time range
* Finding measurements above a threshold
* Finding the latest measurement for a sensor

## In-Memory Implementation

The current implementation stores data in memory using `ConcurrentHashMap`.

Each repository maintains its own store and generates IDs using `AtomicLong`.

```text
AssetRepository
    |
    └── InMemoryAssetRepository
            └── Map<Long, Asset>

SensorRepository
    |
    └── InMemorySensorRepository
            └── Map<Long, Sensor>

MeasurementRepository
    |
    └── InMemoryMeasurementRepository
            ├── Map<Long, Measurement>
            └── Map<Long, List<Measurement>>
                └── sensor ID index
```

## Measurement Index

`InMemoryMeasurementRepository` maintains a secondary index mapping sensor IDs to their measurements.

```text
sensorId → measurements

1 → [Measurement 1, Measurement 4, Measurement 7, ...]
2 → [Measurement 2, Measurement 5, Measurement 8, ...]
```

This allows measurements for a specific sensor to be located directly without scanning the entire measurement store.

## Query Behaviour

Time-based queries return measurements ordered from newest to oldest.

Threshold queries compare measurement values using `BigDecimal`.

Queries that do not find matching records return empty collections or `Optional.empty()` rather than `null`.

## Spring Integration

The concrete repository implementations are registered as Spring beans using `@Repository`.

This allows application services to depend on the repository interfaces while Spring provides the appropriate implementation.

## Future Persistence

The repository interfaces are intentionally independent of the storage mechanism.

The in-memory implementations can later be replaced with PostgreSQL-backed implementations without changing the application layer's repository contract.

```text
Application
     |
     v
Repository Interface
     |
     +── In-Memory Implementation
     |
     └── PostgreSQL Implementation
```
