package com.example.asset_monitoring;

import com.example.asset_monitoring.domain.*;
import com.example.asset_monitoring.repository.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class RepositoryManualTest {

    public static void main(String[] args) {

        // ==========================================
        // CREATE REPOSITORIES
        // ==========================================

        AssetRepository assetRepo = new InMemoryAssetRepository();
        SensorRepository sensorRepo = new InMemorySensorRepository();
        MeasurementRepository measurementRepo =
                new InMemoryMeasurementRepository();


        // ==========================================
        // CREATE ASSETS
        // ==========================================

        Asset asset1 = new Asset("PMP-01", "Feed Pump A");
        Asset asset2 = new Asset("PMP-02", "Feed Pump B");

        assetRepo.save(asset1);
        assetRepo.save(asset2);

        System.out.println("Assets created:");
        System.out.println(asset1);
        System.out.println(asset2);


        // ==========================================
        // CREATE 3 SENSORS
        // ==========================================

        Sensor s1 = new Sensor(
                "S001",
                SensorType.TEMPERATURE,
                new BigDecimal("70"),
                new BigDecimal("90")
        );

        Sensor s2 = new Sensor(
                "S002",
                SensorType.VIBRATION,
                new BigDecimal("8"),
                new BigDecimal("15")
        );

        Sensor s3 = new Sensor(
                "S003",
                SensorType.PRESSURE,
                new BigDecimal("5"),
                new BigDecimal("10")
        );

        asset1.addSensor(s1);
        asset1.addSensor(s2);

        asset2.addSensor(s3);

        sensorRepo.save(s1);
        sensorRepo.save(s2);
        sensorRepo.save(s3);

        System.out.println("\nSensors created:");
        sensorRepo.findAll().forEach(System.out::println);


        // ==========================================
        // CREATE 20 MEASUREMENTS
        // ==========================================

        Instant now = Instant.now();

        Sensor[] sensors = {s1, s2, s3};

        Measurement firstMeasurement = null;

        for (int i = 0; i < 20; i++) {

            Sensor sensor = sensors[i % 3];

            BigDecimal value = new BigDecimal(50 + i);

            Instant recordedAt =
                    now.minusSeconds(i * 60L);

            Measurement measurement = new Measurement(
                    value,
                    sensor,
                    sensor.getType().defaultUnit(),
                    recordedAt
            );

            Measurement saved = measurementRepo.save(measurement);

            if (i == 0) {
                firstMeasurement = saved;
            }
        }

        System.out.println("\n20 measurements created.");
        System.out.println(
                "First generated ID: " + firstMeasurement.getId()
        );


        // ==========================================
        // MEASUREMENT REPOSITORY
        // ==========================================


        // ------------------------------------------
        // 1. findById() — existing ID
        // ------------------------------------------

        System.out.println("\n--- findById() : existing ---");

        System.out.println(
                measurementRepo.findById(firstMeasurement.getId())
        );


        // ------------------------------------------
        // 2. findById() — non-existing ID
        // ------------------------------------------

        System.out.println("\n--- findById() : non-existing ---");

        System.out.println(
                measurementRepo.findById(999L)
        );

        // Expected:
        // Optional.empty


        // ------------------------------------------
        // 3. findBySensorId()
        // ------------------------------------------

        System.out.println("\n--- findBySensorId() : S001 ---");

        List<Measurement> s1Measurements =
                measurementRepo.findBySensorId(s1.getId());

        s1Measurements.forEach(System.out::println);

        // These should appear newest first.


        // ------------------------------------------
        // 4. findBySensorId() — unknown sensor
        // ------------------------------------------

        System.out.println("\n--- findBySensorId() : unknown ---");

        System.out.println(
                measurementRepo.findBySensorId(999L)
        );

        // Expected:
        // []


        // ------------------------------------------
        // 5. findByAssetId()
        // ------------------------------------------

        System.out.println("\n--- findByAssetId() : PMP-01 ---");

        List<Measurement> assetMeasurements =
                measurementRepo.findByAssetId(asset1.getId());

        assetMeasurements.forEach(System.out::println);


        // ------------------------------------------
        // 6. findByAssetId() — unknown asset
        // ------------------------------------------

        System.out.println("\n--- findByAssetId() : unknown ---");

        System.out.println(
                measurementRepo.findByAssetId(999L)
        );

        // Expected:
        // []


        // ------------------------------------------
        // 7. Time-range filtering
        // ------------------------------------------

        System.out.println("\n--- findBySensorIdAndByRecordedAtBetween() ---");

        Instant from = now.minusSeconds(10 * 60L);
        Instant to = now;

        measurementRepo
                .findBySensorIdAndByRecordedAtBetween(
                        s1.getId(),
                        from,
                        to
                )
                .forEach(System.out::println);


        // ------------------------------------------
        // 8. Threshold filtering
        // ------------------------------------------

        System.out.println("\n--- findAboveThreshold() ---");

        measurementRepo
                .findAboveThreshold(
                        asset1.getId(),
                        new BigDecimal("60")
                )
                .forEach(System.out::println);


        // ------------------------------------------
        // 9. Latest measurement
        // ------------------------------------------

        System.out.println("\n--- findLatestBySensorId() ---");

        System.out.println(
                measurementRepo.findLatestBySensorId(
                        s1.getId()
                )
        );


        // ------------------------------------------
        // 10. Latest measurement — unknown sensor
        // ------------------------------------------

        System.out.println("\n--- findLatestBySensorId() : unknown ---");

        System.out.println(
                measurementRepo.findLatestBySensorId(999L)
        );

        // Expected:
        // Optional.empty



        // ==========================================
        // SENSOR REPOSITORY
        // ==========================================


        // ------------------------------------------
        // 11. findById()
        // ------------------------------------------

        System.out.println("\n--- Sensor findById() ---");

        System.out.println(
                sensorRepo.findById(s1.getId())
        );


        // ------------------------------------------
        // 12. findBySerial()
        // ------------------------------------------

        System.out.println("\n--- Sensor findBySerial() ---");

        System.out.println(
                sensorRepo.findBySerial("S001")
        );


        // ------------------------------------------
        // 13. findBySerial() — unknown
        // ------------------------------------------

        System.out.println("\n--- Sensor findBySerial() : unknown ---");

        System.out.println(
                sensorRepo.findBySerial("UNKNOWN")
        );

        // Expected:
        // Optional.empty


        // ------------------------------------------
        // 14. findByAssetId()
        // ------------------------------------------

        System.out.println("\n--- Sensor findByAssetId() ---");

        sensorRepo
                .findByAssetId(asset1.getId())
                .forEach(System.out::println);


        // ==========================================
        // ASSET REPOSITORY
        // ==========================================


        // ------------------------------------------
        // 15. findById()
        // ------------------------------------------

        System.out.println("\n--- Asset findById() ---");

        System.out.println(
                assetRepo.findById(asset1.getId())
        );


        // ------------------------------------------
        // 16. findByCode()
        // ------------------------------------------

        System.out.println("\n--- Asset findByCode() ---");

        System.out.println(
                assetRepo.findByCode("PMP-01")
        );


        // ------------------------------------------
        // 17. existsByCode()
        // ------------------------------------------

        System.out.println("\n--- Asset existsByCode() ---");

        System.out.println(
                assetRepo.existsByCode("PMP-01")
        );

        System.out.println(
                assetRepo.existsByCode("UNKNOWN")
        );

        // Expected:
        // true
        // false


        // ------------------------------------------
        // 18. findAll()
        // ------------------------------------------

        System.out.println("\n--- Asset findAll() ---");

        assetRepo.findAll()
                .forEach(System.out::println);


        // ==========================================
        // FINISHED
        // ==========================================

        System.out.println("\n=================================");
        System.out.println("Repository testing complete.");
        System.out.println("=================================");
    }
}