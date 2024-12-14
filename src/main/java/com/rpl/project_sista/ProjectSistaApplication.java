package com.rpl.project_sista;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ProjectSistaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectSistaApplication.class, args);
	}

}
