package com.denis.fullstack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FullstackApplication {

	public static void main(String[] args) {
		// Start the Spring application
		System.setProperty("java.awt.headless", "false");
		SpringApplication.run(FullstackApplication.class, args);


	}
}
