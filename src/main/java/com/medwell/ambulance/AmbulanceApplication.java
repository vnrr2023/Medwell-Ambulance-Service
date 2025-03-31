package com.medwell.ambulance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AmbulanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmbulanceApplication.class, args);
	}

}
