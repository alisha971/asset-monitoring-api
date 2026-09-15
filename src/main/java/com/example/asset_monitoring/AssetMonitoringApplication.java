package com.example.asset_monitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// import com.example.asset_monitoring.domain.*;
// import com.example.asset_monitoring.exception.IllegalAlertTransitionException;

// import java.math.BigDecimal;
// import java.util.Set;
// import java.util.HashSet;

@SpringBootApplication // starts the spring boot application
public class AssetMonitoringApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssetMonitoringApplication.class, args);

		// // Domain Model Testing
		// // Create asset
		// Asset asset = new Asset("PMP-01", "Feed Pump A");
		// System.out.println("Asset Created: " + asset);
		
		// // Add Sensors
		// Sensor s1 = new Sensor("S001", SensorType.TEMPERATURE, new BigDecimal("70"), new BigDecimal("90"));
		// Sensor s2 = new Sensor("S002", SensorType.VIBRATION, new BigDecimal("8"), new BigDecimal("15"));
		// asset.addSensor(s1);
		// asset.addSensor(s2);
		// System.out.println("Sensors for " + asset.getCode() + ": " + asset.getSensors());

		// Measurement m1 = new Measurement(new BigDecimal("95"), s1, "", null);
		// System.out.println("Measurement recorded: " + m1);
		// if(!s1.isWithinLimits(m1.getValue())){
		// 	Alert a1 = new Alert(s1, s1.severityOf(m1.getValue()));
		// 	System.out.println(a1);

		// 	try{
		// 		System.out.println("Trying to resolve alert....");
		// 		a1.resolve();
		// 	}catch (IllegalAlertTransitionException e){
		// 		System.out.println("Error: " + e.getMessage());
		// 	}
			
		// 	System.out.println("Acknowledging the alert...");
		// 	a1.acknowledge();
		// 	System.out.println("Alert Acknowledged at " + a1.ackAt() + ", State: " + a1.getState());
		// 	System.out.println("Resolving the alert...");
		// 	a1.resolve();
		// 	System.out.println("Alert resolved at " + a1.resolvedAt() + ", State: " + a1.getState());
		// }
		
		// Set<Asset> set = new HashSet<>();
		// set.add(asset);
		// set.add(new Asset("PMP-01", "Feed Pump B"));
		// System.out.println(set.size() == 1);

		// OUTPUT
		// Asset Created: Asset{id = null, code = PMP-01, name = Feed Pump A, status = ACTIVE
		// Sensors for PMP-01: [Sensor: {id:null, serial: S001, type: TEMPERATURE, active: true, warning: 70, critical: 90, Sensor: {id:null, serial: S002, type: VIBRATION, active: true, warning: 8, critical: 15]
		// Measurement recorded: Measurement{id: null, sensor: S001, value: 95 degC, recordedAt: 2026-09-15T14:34:28.190521900Z}
		// Alert{id: null, state: CRITICAL, severity: CRITICAL, sensor: Sensor: {id:null, serial: S001, type: TEMPERATURE, active: true, warning: 70, critical: 90, raisedAt: 2026-09-15T14:34:28.195519900Z
		// Trying to resolve alert....
		// Error: Cannot transition from CRITICAL to RESOLVED
		// Acknowledging the alert...
		// Alert Acknowledged at 2026-09-15T14:34:28.201517300Z, State: ACKNOWLEDGEMENT
		// Resolving the alert...
		// Alert resolved at 2026-09-15T14:34:28.201517300Z, State: RESOLVED
		// true

		
	}

}

@RestController //tells that this class contains http endpoints
class HealthController{
	
	@GetMapping("/ping") //maps get req to "/ping" to this function
	public String ping(){
		return "asset-monitoring up";
	}
	@GetMapping("/ping/version")
	public VersionResponse pingVersion(){
		VersionResponse response = new VersionResponse("asset-monitoring", "0.0.1");
		return response;
	}
}

record VersionResponse(String name, String version){
}



	