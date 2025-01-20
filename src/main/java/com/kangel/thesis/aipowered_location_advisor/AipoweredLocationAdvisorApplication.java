package com.kangel.thesis.aipowered_location_advisor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@SpringBootApplication
@Controller
public class AipoweredLocationAdvisorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AipoweredLocationAdvisorApplication.class, args);
	}

	@GetMapping("/index")
	public String getMethodName() {
		return "index";
	}
	
}
