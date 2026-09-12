package com.example.asset_monitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication // starts the spring boot application
public class AssetMonitoringApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssetMonitoringApplication.class, args);
	}

}

@RestController //tells that this class contains http endpoints
class HealthController{
	
	@GetMapping("/ping") //maps get req to "/ping" to this function
	public String ping(){
		return "asset-monitoring up";
	}
}