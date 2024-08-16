package com.ASDC.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RoomM8Application {

	public static void main(String[] args) {
		SpringApplication.run(RoomM8Application.class, args);
	}

}
